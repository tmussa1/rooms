package com.mc.roomlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Autowired
    RoomRepository roomRepository;

    @RequestMapping("/")
    public String showHomepage(Model model){
        model.addAttribute("rooms", roomRepository.findAll());
        return "index";
    }

    @RequestMapping("/addroom")
    public String addRoom(Model model){
        model.addAttribute("room", new Room());
        return "addroom";
    }

    @RequestMapping("/saveroom")
    public String saveRoom(@ModelAttribute("room") Room room, Model model){
        roomRepository.save(room);
        model.addAttribute("rooms", roomRepository.findAll());
        return "index";
    }

    @RequestMapping("/updateroom/{id}")
    public String updateRoom(@PathVariable("id") long id, Model model){
        Room room = roomRepository.findById(id).get();
        model.addAttribute("room", room);
        roomRepository.save(room);
        return "addroom";
    }
    @RequestMapping("/details/{id}")
    public String showDetails(@PathVariable("id") long id, Model model){
        Room room = roomRepository.findById(id).get();
        model.addAttribute("room", room);
        return "detail";
    }


}
