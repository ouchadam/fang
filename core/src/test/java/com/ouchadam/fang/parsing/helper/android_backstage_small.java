package com.ouchadam.fang.parsing.helper;

import com.ouchadam.fang.FangCalendar;
import com.ouchadam.fang.domain.channel.Image;
import com.ouchadam.fang.domain.item.Audio;

public class android_backstage_small implements XmlValues {

    @Override
    public String channelTitle() {
        return "Android Developers Backstage";
    }

    @Override
    public String channelCategory() {
        return "Technology/Tech News";
    }

    @Override
    public Image channelImage() {
        return new Image("http://commondatastorage.googleapis.com/androiddevelopers/PodCastCover_1.png", null, null, 0, 0);
    }

    @Override
    public String channelSummary() {
        return "Android Backstage, a podcast by and for Android developers. Hosted by\n" +
                "            developers from the Android engineering team, this show covers topics of interest to\n" +
                "            Android programmers, with in-depth discussions and interviews with engineers on the\n" +
                "            Android team at Google.";
    }

    @Override
    public int itemCount() {
        return 8;
    }

    @Override
    public String firstItemTitle() {
        return "Android Developers Backstage - Episode 8: Volley";
    }

    @Override
    public String firstItemLink() {
        return "                http://feedproxy.google.com/~r/blogspot/AndroidDevelopersBackstage/~3/UpR0tPpmNqY/android-developers-backstage-episode-8.html";
    }

    @Override
    public FangCalendar firstItemPubDate() {
        return new FangCalendar("Thu, 29 May 2014 12:01:22 PDT");
    }

    @Override
    public Audio firstItemAudio() {
        return new Audio("http://storage.googleapis.com/androiddevelopers/android_developers_backstage/Android%20Developers%20Backstage%20Ep%208%20Volley.mp3", "audio/mpeg");
    }

    @Override
    public String firstItemSubtitle() {
        return "In this episode, the second in a bizarrely and completely unplanned\n" +
                "                series on Play Store technology, Tor Norbye and Chet Haase are joined by Ficus\n" +
                "                Kirkpatrick from the Play Store team (and from many other Android projects from the\n" +
                "                early days). Listen in";
    }

    @Override
    public String firstItemSummary() {
        return "In this episode, the second in a bizarrely and completely unplanned\n" +
                "                series on Play Store technology, Tor Norbye and Chet Haase are joined by Ficus\n" +
                "                Kirkpatrick from the Play Store team (and from many other Android projects from the\n" +
                "                early days). Listen in to hear what Volley is for and how you can use it to simplify\n" +
                "                your network requests and bitmap download/caching code. Relevant links: Volley:\n" +
                "                https://android.googlesource.com/platform/frameworks/volley/ Volley email list:\n" +
                "                https://groups.google.com/forum/#!forum/volley-users Gson adapter for Volley:\n" +
                "                https://gist.github.com/ficusk/5474673 Subscribe to the podcast at\n" +
                "                http://feeds.feedburner.com/blogspot/AndroidDevelopersBackstage﻿. Or download\n" +
                "                directly at\n" +
                "                http://storage.googleapis.com/androiddevelopers/android_developers_backstage/Android%20Developers%20Backstage%20Ep%208%20Volley.mp3\n" +
                "           ";
    }
}
