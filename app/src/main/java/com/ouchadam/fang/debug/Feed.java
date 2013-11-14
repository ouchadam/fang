package com.ouchadam.fang.debug;

class Feed {

    String url;
    String channelTitle;
    int oldItemCount;

    Feed() {
        this.url = null;
        this.channelTitle = null;
        this.oldItemCount = 0;
    }

    boolean hasChannelTitle() {
        return channelTitle != null;
    }

}
