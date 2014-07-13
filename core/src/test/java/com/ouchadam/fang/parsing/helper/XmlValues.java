package com.ouchadam.fang.parsing.helper;

import com.ouchadam.fang.FangCalendar;
import com.ouchadam.fang.domain.channel.Image;
import com.ouchadam.fang.domain.item.Audio;

import java.util.List;

public interface XmlValues {
    String channelTitle();
    List<String> channelCategory();
    Image channelImage();
    String channelSummary();
    int itemCount();
    String firstItemTitle();
    String firstItemLink();
    FangCalendar firstItemPubDate();
    Audio firstItemAudio();
    String firstItemSubtitle();
    String firstItemSummary();
}
