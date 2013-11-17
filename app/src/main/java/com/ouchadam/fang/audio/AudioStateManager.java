package com.ouchadam.fang.audio;

class AudioStateManager {

    private PlayState playState;
    private PositionState positionState;

    private enum PlayState {
        PLAYING,
        IDLE;
    }

    private enum PositionState {
        VANILLA,
        SHIFTED;
    }

    AudioStateManager() {
        this.playState = PlayState.IDLE;
        this.positionState = PositionState.VANILLA;
    }

    public void setPlaying() {
        this.playState = PlayState.PLAYING;
    }

    public void setIdle() {
        this.playState = PlayState.IDLE;
    }

    public boolean isPlaying() {
        return this.playState == PlayState.PLAYING;
    }

    public void setPositionVanilla() {
        this.positionState = PositionState.VANILLA;
    }

    public void setPositionShifted() {
        this.positionState = PositionState.SHIFTED;
    }

    public boolean hasVanillaPosition() {
        return positionState == PositionState.VANILLA;
    }

}
