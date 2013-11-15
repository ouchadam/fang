package com.ouchadam.fang.audio;

import com.ouchadam.fang.audio.event.PlayerEvent;

interface AudioSync {
    void onSync(long itemId, PlayerEvent playerEvent);
}
