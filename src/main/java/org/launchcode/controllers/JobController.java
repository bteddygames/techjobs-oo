package org.launchcode.controllers;

import java.util.HashMap;
import org.launchcode.models.Job;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, int id) {

        Job job = jobData.findById(id);

        HashMap<String,String> result = new HashMap<String,String>();

        result.put("Name",job.getName());
        result.put("Employer",job.getEmployer().toString());
        result.put("Location",job.getLocation().toString());
        result.put("Position Type", job.getPositionType().toString());
        result.put("Skill",job.getCoreCompetency().toString());

        model.addAttribute("job",result);

        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @Valid JobForm jobForm, Errors errors) {

        if(errors.hasErrors()){
            return "new-job";
        }

        Job newJob = new Job();

        newJob.setName(jobForm.getName());
        newJob.setEmployer(jobData.getEmployers().findById(jobForm.getEmployerId()));
        newJob.setLocation(jobData.getLocations().findById(jobForm.getLocationId()));
        newJob.setCoreCompetency(jobData.getCoreCompetencies().findById(jobForm.getSkillId()));
        newJob.setPositionType(jobData.getPositionTypes().findById(jobForm.getPositionId()));

        jobData.add(newJob);

        return "redirect:?id="+newJob.getId();

    }
}
