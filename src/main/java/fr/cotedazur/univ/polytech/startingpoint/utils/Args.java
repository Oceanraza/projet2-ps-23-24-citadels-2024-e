package fr.cotedazur.univ.polytech.startingpoint.utils;

import com.beust.jcommander.Parameter;

public class Args {
    @Parameter(names = "--2thousands", description = "2 x 1000 games")
    private boolean is2Thousands;

    @Parameter(names = "--demo", description = "Demo mode (used for the presentation)")
    private boolean isDemo;

    public boolean is2Thousands() {
        return is2Thousands;
    }

    public boolean isDemo() {
        return isDemo;
    }
}