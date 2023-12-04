package com.example.utils;

public class FrameMetaData {

    private int width;
    private int height;
    private int rotation;
    private int cameraFacing;

    // Constructor private
    private FrameMetaData(int width, int height, int rotation, int cameraFacing) {
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        this.cameraFacing = cameraFacing;
    }

    // Setter dan Getter untuk masing-masing properti

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getRotation() {
        return rotation;
    }

    public void setCameraFacing(int cameraFacing) {
        this.cameraFacing = cameraFacing;
    }

    public int getCameraFacing() {
        return cameraFacing;
    }

    // Builder class untuk FrameMetadata

    public static class Builder {

        private int width = 0;
        private int height = 0;
        private int rotation = 0;
        private int cameraFacing = 0;

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setRotation(int rotation) {
            this.rotation = rotation;
            return this;
        }

        public Builder setCameraFacing(int facing) {
            cameraFacing = facing;
            return this;
        }

        public FrameMetaData build() {
            return new FrameMetaData(width, height, rotation, cameraFacing);
        }
    }
}