package com.pengembangsebelah.stmmappxo.model;

import java.util.List;

public class ScheduleProgram {
    public List<ProgramModel> programModels;
    public String day;

    public ScheduleProgram(){}
    public ScheduleProgram(String day,List<ProgramModel> programModels){
        this.day=day;
        this.programModels=programModels;
    }
}
