package com.example.appengine.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/app")
public class MatchController
{
    @Autowired
    MatchService matchService;
    @RequestMapping(value="/match/save",method=RequestMethod.POST)
    @ResponseBody
    public String save(@RequestBody Match match)  //Controller to save the balls details to the firebase
    {
        try
        {
            String res=matchService.addMatchEstimation(match);
            return res;

        }catch (Exception e)
        {
            return e.getMessage();
        }
    }

    @GetMapping("/getTotalRunsScoredByEachTeam")
    @ResponseBody
    public Object getTotalRunsScoredByEachTeam() //Controller to get the total runs scored by each team
    {
        try
        {
            return matchService.getTotalRunsScoredByEachTeam();
        }catch (Exception e)
        {
            return e.getMessage();
        }
    }

    @GetMapping("/getTotalRunsScoredInEachOver")
    @ResponseBody
    public Object getTotalRunsScoredInEachOver() //Controller to get the total runs scored in each over
    {
        try
        {
            return matchService.getTotalRunsScoredInEachOver();
        }catch (Exception e)
        {
            return e.getMessage();
        }
    }

    @GetMapping("/getTotalRunsScoredByEachBatsman")
    @ResponseBody
    public Object getTotalRunsScoredByEachBatsman() //Controller to get total runs scored by each batsman
    {
        try
        {
            return matchService.getTotalRunsScoredByEachBatsman();
        }catch (Exception e)
        {
            return e.getMessage();
        }
    }




}
