package gui;

import utilities.parsedclass.GameVisualizerData;

public interface IMainApp {
    GameVisualizerData getGameVisualizer();
    void setGameVisualizer(GameVisualizerData gv);
}
