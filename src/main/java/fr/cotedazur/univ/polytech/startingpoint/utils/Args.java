package fr.cotedazur.univ.polytech.startingpoint.utils;

import com.beust.jcommander.Parameter;

public class Args {
    @Parameter(names = "--2thousands", description = "2 x 1000 games")
    private boolean is2Thousands;

    @Parameter(names = "--demo", description = "Demo mode (used for the presentation)")
    private boolean isDemo;

    @Parameter(names = "--csv", description = "Saving data mode")
    private boolean isCsv;
    public ArgsEnum getCurrentMode(){
        if (isCsv){
            return ArgsEnum.CSV;
        }
        else if (is2Thousands){
            return ArgsEnum.TWOTHOUSANDS;}
        else{
            return ArgsEnum.TWOTHOUSANDS;
        }
    }
    public enum ArgsEnum {
        TWOTHOUSANDS(),
        CSV(),
        DEMO();

    }
}