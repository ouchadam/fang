package com.ouchadam.fang.parsing.helper;

import com.ouchadam.fang.domain.channel.Image;
import com.ouchadam.fang.domain.item.Audio;

public interface XmlValues {
    String channelTitle();
    String channelCategory();
    Image channelImage();
    String channelSummary();
    int itemCount();
    String firstItemTitle();
    String firstItemLink();
    String firstItemPubDate();
    Audio firstItemAudio();
    String firstItemSubtitle();
    String firstItemSummary();
}
