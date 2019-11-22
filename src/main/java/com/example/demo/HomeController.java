package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    FlightRepository flightRepository;

    @Autowired
    CloudinaryConfig cloudc;

    @RequestMapping("/")
    public String flightList(Model model) {
        model.addAttribute("flights", flightRepository);
        return "flightList";
    }

    @GetMapping("/add")
    public String flightForm(Model model) {
        model.addAttribute("flight" , new Flight());
        return "flightForm";
    }

    @PostMapping("/process")
    public String processForm(@Valid Flight flight, BindingResult result) {
        if(result.hasErrors()) {
            return "flightForm";
        }
        flightRepository.save(flight);
        return "redirect:/";
    }

    @PostMapping("/add")
    public String processImage(@ModelAttribute Flight flight, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            flight.setImage(null);
        } else {

            try {
                Map uploadResult = cloudc.upload(file.getBytes(),
                        ObjectUtils.asMap("resourcetype", "auto"));
                flight.setImage(uploadResult.get("url").toString());

            } catch (IOException e) {
                e.printStackTrace();
                flight.setImage(null);
            }
        }
        messageRepository.save(message);
        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showMessage(@PathVariable("id") long id, Model model) {
        model.addAttribute("flight" , flightRepository.findById(id).get());
        return "view";
    }
    @RequestMapping("/update/{id}")
    public String updateMessage(@PathVariable("id") long id,
                                Model model){
        model.addAttribute("flight", flightRepository.findById(id).get());
        return "flightForm";
    }

    @RequestMapping("/delete/{id}")
    public String deleteMessage(@PathVariable("id") long id){
        flightRepository.deleteById(id);
        return "redirect:/";
    }
}
