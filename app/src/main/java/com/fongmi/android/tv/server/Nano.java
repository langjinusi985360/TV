package com.fongmi.android.tv.server;

import com.fongmi.android.tv.api.config.LiveConfig;
import com.fongmi.android.tv.bean.Device;
import com.fongmi.android.tv.server.impl.Process;
import com.fongmi.android.tv.server.process.Action;
import com.fongmi.android.tv.server.process.Cache;
import com.fongmi.android.tv.server.process.Image;
import com.fongmi.android.tv.server.process.Local;
import com.fongmi.android.tv.server.process.Media;
import com.fongmi.android.tv.server.process.Parse;
import com.fongmi.android.tv.server.process.Proxy;
import com.github.catvod.utils.Asset;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class Nano extends NanoHTTPD {

    private static final String INDEX = "index.html";
    private static final String[] PRIVATE_PATHS = {"/action", "/cache", "/file", "/image", "/media", "/newFolder", "/upload", "/delFolder", "/delFile", "/parse", "/proxy"};

    private List<Process> process;

    public Nano(int port) {
        super(port);
        addProcess();
    }

    private void addProcess() {
        process = new ArrayList<>();
        process.add(new Action());
        process.add(new Cache());
        process.add(new Image());
        process.add(new Local());
        process.add(new Media());
        process.add(new Parse());
        process.add(new Proxy());
    }

    public static Response ok() {
        return ok("OK");
    }

    public static Response ok(String text) {
        return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT, text);
    }

    public static Response error(String text) {
        return error(Response.Status.INTERNAL_ERROR, text);
    }

    public static Response error(Response.Status status, String text) {
        return newFixedLengthResponse(status, MIME_PLAINTEXT, text);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String url = session.getUri().trim();
        if (!isLoopback(session) && isPrivatePath(url)) return error(Response.Status.FORBIDDEN, "Forbidden");
        Map<String, String> files = new HashMap<>();
        if (session.getMethod() == Method.POST) parse(session, files);
        if (url.startsWith("/tvbus")) return ok(LiveConfig.getResp());
        if (url.startsWith("/device")) return ok(Device.get().toString());
        for (Process process : process) if (process.isRequest(session, url)) return process.doResponse(session, url, files);
        return getAssets(url.substring(1));
    }

    private static boolean isPrivatePath(String url) {
        for (String path : PRIVATE_PATHS) {
            if (url.equals(path) || url.startsWith(path + "/") || url.startsWith(path + "?")) return true;
        }
        return false;
    }

    private static boolean isLoopback(IHTTPSession session) {
        String address = session.getRemoteIpAddress();
        return address != null && (address.startsWith("127.") || address.equals("::1") || address.equals("0:0:0:0:0:0:0:1"));
    }

    private void parse(IHTTPSession session, Map<String, String> files) {
        try {
            String ct = session.getHeaders().get("content-type");
            if (ct != null) session.getHeaders().put("content-type", ct.replace("multipart/form-data", "multipart/form-data; charset=utf-8"));
            session.parseBody(files);
        } catch (Exception ignored) {
        }
    }

    private Response getAssets(String path) {
        try {
            if (path.isEmpty()) path = INDEX;
            InputStream is = Asset.open(path);
            return newFixedLengthResponse(Response.Status.OK, getMimeTypeForFile(path), is, -1);
        } catch (Exception e) {
            return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_HTML, null, 0);
        }
    }
}
