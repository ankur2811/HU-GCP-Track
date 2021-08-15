package com.example.appengine.springboot;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import feign.Headers;
@Headers("Content-Type: application/json")
@FeignClient(name="spring-gcp-1",url="https://ankur2021211first-dot-hu18-groupb-angular.et.r.appspot.com")
public interface matchService
{
    @RequestMapping(value="/app/match/save",method=RequestMethod.POST)
    String save(@RequestBody Match match);
    @RequestMapping(value="/app/getTotalRunsScoredByEachTeam",method=RequestMethod.GET)
    Object getTotalRunsScoredByEachTeam();
    @RequestMapping(value="/app/getTotalRunsScoredInEachOver",method=RequestMethod.GET)
    Object getTotalRunsScoredInEachOver();
    @RequestMapping(value="/app/getTotalRunsScoredByEachBatsman",method=RequestMethod.GET)
    Object getTotalRunsScoredByEachBatsman();



}
