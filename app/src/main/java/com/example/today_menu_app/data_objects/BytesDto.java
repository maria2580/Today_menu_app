package com.example.today_menu_app.data_objects;

import com.google.gson.annotations.SerializedName;

public class BytesDto {
    @SerializedName("png") private byte[] bytes;

    public BytesDto() {
    }

    public BytesDto(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
