package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class HomeController {
    @Autowired
    FlightRepository flightRepository;
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
