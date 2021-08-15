package com.example.appengine.springboot;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app1")
public class matchController
{
    @Autowired
    matchService restClientService;
    @RequestMapping(value="/match/save",method=RequestMethod.POST)
    @ResponseBody
    public String save(@RequestBody Match match)
    {
        return restClientService.save(match);
    }

    @RequestMapping(value="/getTotalRunsScoredByEachTeam",method=RequestMethod.GET)
    @ResponseBody
    public Object getrunbyteam()
    {
        return restClientService.getTotalRunsScoredByEachTeam();
    }

    @RequestMapping(value="/getTotalRunsScoredInEachOver",method=RequestMethod.GET)
    @ResponseBody
    public Object getrunbyover()
    {
        return restClientService.getTotalRunsScoredInEachOver();
    }

    @RequestMapping(value="/getTotalRunsScoredByEachBatsman",method=RequestMethod.GET)
    @ResponseBody
    public Object getrunbybatsman()
    {
        return restClientService.getTotalRunsScoredByEachBatsman();
    }




}
