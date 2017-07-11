package pl.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.test.model.Message;
import pl.test.service.MessagesService;

import java.util.List;

/**
 * Created by MSI on 18.06.2017.
 */
@Controller
@RequestMapping("/")
public class ChatController {

    @Autowired
    private MessagesService service;

    @RequestMapping
    public String showMain(Model model) {
        return "main";
    }

    @RequestMapping(value = "/data/group/{name}")
    @ResponseBody
    public List<Message> getGrpupMsgs(@PathVariable String name) {
        List<Message> list = service.loadGroupMessages(name);
        return list;
    }

    @RequestMapping(value = "/data/private")
    @ResponseBody
    public List<Message> getPrivateMsgs(@RequestParam("u1") String user1, @RequestParam("u2") String user2) {
        List<Message> list = service.loadPrivateMessages(user1, user2);
        return list;
    }

    @RequestMapping(value = "/data/login", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public boolean login(@RequestBody String body) {
        System.out.println(body);
        return service.validateUser(body);
    }

    @RequestMapping(value = "/data/register", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public boolean register(@RequestBody String body) {
        return service.createNewUser(body);
    }

    @RequestMapping(value = "/data/send", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public boolean sendGroupMsg(@RequestBody String body) {
        return service.addMsg(body);
    }
}
