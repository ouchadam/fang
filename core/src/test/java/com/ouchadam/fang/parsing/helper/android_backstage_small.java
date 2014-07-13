package com.ouchadam.fang.parsing.helper;

import com.ouchadam.fang.FangCalendar;
import com.ouchadam.fang.domain.channel.Image;
import com.ouchadam.fang.domain.item.Audio;

import java.util.List;

public class android_backstage_small implements XmlValues {

    @Override
    public String channelTitle() {
        return "Android Developers Backstage";
    }

    @Override
    public List<String> channelCategory() {
        return Util.categoryList("Technology", "Tech News");
    }

    @Override
    public Image channelImage() {
        return new Image("http://commondatastorage.googleapis.com/androiddevelopers/PodCastCover_1.png", "", "", 0, 0);
    }

    @Override
    public String channelSummary() {
        return "Android Backstage, a podcast by and for Android developers. Hosted by developers from the Android engineering team, this show covers topics of interest to Android programmers, with in-depth discussions and interviews with engineers on the Android team at Google.";
    }

    @Override
    public int itemCount() {
        return 9;
    }

    @Override
    public String firstItemTitle() {
        return "Android Developers Backstage - Episode 9: Design";
    }

    @Override
    public String firstItemLink() {
        return "http://feedproxy.google.com/~r/blogspot/AndroidDevelopersBackstage/~3/LUu8ny4NEiU/android-developers-backstage-episode-9.html";
    }

    @Override
    public FangCalendar firstItemPubDate() {
        return new FangCalendar("Thu, 29 May 2014 11:46:37 PDT");
    }

    @Override
    public Audio firstItemAudio() {
        return new Audio("http://storage.googleapis.com/androiddevelopers/android_developers_backstage/Android%20Developers%20Backstage%20Ep9%20Design.mp3", "audio/mpeg");
    }

    @Override
    public String firstItemSubtitle() {
        return "Tor and Chet make a startling break with ancient tradition and talk to a real, live designer in this episode: Christian Robertson from the Android User Experience team. Tune in to hear about the Roboto font that Christian created and about font design in";
    }

    @Override
    public String firstItemSummary() {
        return "Tor and Chet make a startling break with ancient tradition and talk to a real, live designer in this episode: Christian Robertson from the Android User Experience team. Tune in to hear about the Roboto font that Christian created and about font design in general, plus design tips for layout, visual rhythm, and other Android designities. Design: It's the new Develop. Relevant links: Android Design: https://developer.android.com/design/index.html Android Style Guide: http://developer.android.com/design/style/index.html Android Asset Studio: http://romannurik.github.io/AndroidAssetStudio/ Roboto: http://www.google.com/fonts/specimen/Roboto Christian: https://plus.google.com/110879635926653430880/posts Tor: google.com/+TorNorbye Chet: google.com/+ChetHaase Subscribe to the podcast in your favorite player or at&nbsp;http://feeds.feedburner.com/blogspot/AndroidDevelopersBackstage Or just download the mp3 directly: http://storage.googleapis.com/androiddevelopers/android_developers_backstage/Android%20Developers%20Backstage%20Ep9%20Design.mp3";
    }
}
