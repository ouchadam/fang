package com.ouchadam.sprsrspodcast.parsing.helper;

import com.ouchadam.sprsrspodcast.domain.channel.Image;
import com.ouchadam.sprsrspodcast.domain.item.Audio;

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
