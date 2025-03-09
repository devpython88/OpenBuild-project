/*
 * Decompiled with CFR 0.152.
 */
package org.seriouz.openbuild.properties;

public class DoorProperties {
    public String opened;
    public String closed;

    public DoorProperties(String opened, String closed) {
        if (closed.contains("Open")) {
            this.opened = closed;
            this.closed = opened;
            return;
        }
        this.opened = opened;
        this.closed = closed;
    }
}
