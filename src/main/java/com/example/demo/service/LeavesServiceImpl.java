package com.example.demo.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.demo.dto.LeaveBalanceDTO;
import com.example.demo.dto.LeaveDTO;
import com.example.demo.mailgun.MailgunService;
import com.example.demo.models.Employees;
import com.example.demo.models.Leaves;
import com.example.demo.nexmosms.NexmoService;
import com.example.demo.repos.EmployeeRepo;
import com.example.demo.repos.LeavesRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LeavesServiceImpl implements LeavesService {

	private final EmployeeRepo employeesRepository;

	private final LeavesRepository leavesRepository;

    private final MailgunService mailgunService;
    
    private final NexmoService nexmoService;
    
    
    public LeavesServiceImpl(EmployeeRepo employeesRepository, LeavesRepository leavesRepository, MailgunService mailgunService, NexmoService nexmoService) {
    	this.employeesRepository = employeesRepository;
    	this.leavesRepository= leavesRepository;
    	this.mailgunService = mailgunService;
    	this.nexmoService =nexmoService;
    }
    
    
    private static final String APPROVED = "APPROVED";
    private static final String PENDING = "PENDING";
    private static final String DENIED = "DENIED";
    private static final String ADMIN_MOBILE = "+919652261423";


   
    
    
	
	@Override
	public void addLeaves(String authenticatedEmployeeId, LeaveDTO leaveDTO) {
	    Optional<Employees> optionalEmployee = employeesRepository.findById(authenticatedEmployeeId);

	    if (optionalEmployee.isPresent()) {
	        Employees employee = optionalEmployee.get();

//	        // Check if there are pending leaves
//	        boolean hasPendingLeaves = employee.getLeaves().stream()
//	                .anyMatch(leave -> leave.getStatus().equals(PENDING));
//
//	        if (hasPendingLeaves) {
//	            throw new IllegalStateException("You already have pending leave(s). Please look into them first.");
//	        }

	        // Proceed to add new leave
	        Leaves leave = new Leaves();
	        leave.setTypeOfLeave(leaveDTO.getTypeOfLeave());
	        leave.setReason(leaveDTO.getReason());
	        leave.setStatus(PENDING);
	        leave.setAppliedDate(LocalDate.now()); // Set current date for applied date
	        leave.setDays(leaveDTO.getDays()); // Set days from leaveDTO

	        if (leaveDTO.getDays() == 1) {
	            leave.setDateOfLeave(leaveDTO.getDateOfLeave());
	            leave.setEndDate(leaveDTO.getDateOfLeave()); // endDate is same as dateOfLeave for single day
	        } else {
	            leave.setDateOfLeave(leaveDTO.getStartDate());
	            leave.setEndDate(leaveDTO.getEndDate());
	        }

	        // Calculate approved days for the employee
	        int approvedDays = employee.getLeaves().stream()
	                .filter(l -> l.getStatus().equals(APPROVED))
	                .mapToInt(Leaves::getDays)
	                .sum();

	        // Set availableLeaves based on calculation
	        int availableLeaves = employee.getTotalLeaves() - approvedDays;
	        if (availableLeaves <= 0) {
	            throw new IllegalArgumentException("You have 0 available leaves. Please contact administration.");
	        } else {
	            leave.setAvailableLeaves(availableLeaves);
//	            leave.setApprovedDays(approvedDays); // Set approved days

	            leave.setEmployee(employee);
	            System.out.println("this is print statements: "+employee);
	            employee.getLeaves().add(leave);

	            employeesRepository.save(employee);
	            //Mail request 
	            String from =employee.getEmail();
		        String to = "kommukirankumar1226@gmail.com";
		        String subject = "Request for "+leaveDTO.getTypeOfLeave()+" With EmployeeID : "+employee.getEmployeeId();
		        mailgunService.sendSimpleEmail(from, to, subject, authenticatedEmployeeId);
		        
		        String daysText;
		        if (leaveDTO.getDays() == 1) {
		            daysText = "1 day on " + leaveDTO.getDateOfLeave();
		        } else {
		            daysText = leaveDTO.getDays() + " days from " + leaveDTO.getDateOfLeave() + " to " + leaveDTO.getEndDate();
		        }

		        String[] recipientRoles = {"Admin", "Manager", "HR"};
		        String[] recipientNumbers = {ADMIN_MOBILE, "+917780164901", "+916304231585"};

		        for (int i = 0; i < recipientRoles.length; i++) {
		            String recipientRole = recipientRoles[i];
		            String recipientNumber = recipientNumbers[i];

		            String text = "Hi " + recipientRole + ",\n" +
		                          "I am " + employee.getFirstname() + " with EmployeeID: " + employee.getEmployeeId() + "\n" +
		                          "I need leave for " + daysText + " for the reason: " + leaveDTO.getReason() + "\n" +
		                          "Please consider my request and grant the leave.\n" +
		                          "Thank you.";
		            nexmoService.sendSms(recipientNumber, text);
		        }
	        
	        }
	        }

	    else {
	        throw new IllegalArgumentException("Authenticated employee not found.");
	    }
	}



	    

	@Override
	public List<LeaveDTO> getPendingLeaves() {
		List<Leaves> pendingLeaves = leavesRepository.findByStatus(PENDING);
		return pendingLeaves.stream().map(leave -> new LeaveDTO(leave.getLeaveId(),leave.getTypeOfLeave(), leave.getReason(),
				leave.getDateOfLeave(), leave.getAppliedDate(), leave.getEmployee().getEmployeeId(), leave.getAvailableLeaves(),leave.getStatus(),leave.getEndDate()))
				.toList();
	}
	@Override
	public void approveLeave(Integer leaveId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String authenticatedEmployeeId = auth.getName();
		
	    Optional<Leaves> optionalLeave = leavesRepository.findById(leaveId);

	    if (optionalLeave.isPresent()) {
	        Leaves leave = optionalLeave.get();
	        Employees employee = leave.getEmployee();

	        // Number of days of the approved leave
	        int approvedDays = leave.getDays();

	        // Approve the leave
	        leave.setStatus(APPROVED);
	        leavesRepository.save(leave);

	        // Find the leave requester (Kiran)
	        Employees requester = employeesRepository.findById(authenticatedEmployeeId)
	            .orElseThrow(() -> new IllegalArgumentException("Authenticated employee not found."));

	        // Send notification email to the leave requester (Kiran)
	        String from = requester.getEmail();
	        String to = employee.getEmail();
	        String subject = "Leave Approved for " + leave.getTypeOfLeave() + " - EmployeeID: " + employee.getEmployeeId();
	        String message = "Your leave request for " + leave.getDays() + " days has been approved.";
	        mailgunService.sendSimpleEmail(from, to, subject, message);

	        // Send notification SMS to the leave requester (Kiran)
	        String recipientNumber = ADMIN_MOBILE; // Change this to Kiran's phone number
	        String smsText = "Your leave request for " + leave.getDays() + " days has been approved.";
	        nexmoService.sendSms(recipientNumber, smsText);
	    } else {
	        throw new IllegalArgumentException("Leave not found.");
	    }
	}



	@Override
	public void denyLeave(Integer leaveId, String denyReason) {
		Authentication auth= SecurityContextHolder.getContext().getAuthentication();
		String authenticatedEmployeeId =auth.getName();
	    Optional<Leaves> optionalLeave = leavesRepository.findById(leaveId);

	    if (optionalLeave.isPresent()) {
	        Leaves leave = optionalLeave.get();

	        // Deny the leave
	        leave.setStatus(DENIED);
	        leave.setReason(denyReason);
	        leavesRepository.save(leave);

	        // Find the leave requester (Kiran)
	        Employees requester = employeesRepository.findById(authenticatedEmployeeId)
	            .orElseThrow(() -> new IllegalArgumentException("Authenticated employee not found."));

	        // Send notification email to the leave requester (Kiran)
	        String from = "kommukirankumar1226@gmail.com";
	        //requester.getEmail();
	        String to = leave.getEmployee().getEmail();
	        String subject = "Leave Denied for " + leave.getTypeOfLeave() + " - EmployeeID: " + leave.getEmployee().getEmployeeId();
	        String message = "Your leave request for " + leave.getDays() + " days has been denied. Reason: " + denyReason;
	        mailgunService.sendSimpleEmail(from, to, subject, message);

	        // Send notification SMS to the leave requester (Kiran)
	        String recipientNumber = ADMIN_MOBILE; // Change this to Kiran's phone number
	        String smsText = "Your leave request for " + leave.getDays() + " days has been denied. Reason: " + denyReason;
	        nexmoService.sendSms(recipientNumber, smsText);
	    } else {
	        throw new IllegalArgumentException("Leave not found.");
	    }
	}

    
    

	@Override
    public List<LeaveDTO> getMyLeaves(String employeeId) {
        Employees employee = employeesRepository.findById(employeeId).orElseThrow(() -> new IllegalArgumentException("Employee not found."));
        return employee.getLeaves().stream()
                .map(leave -> new LeaveDTO(leave.getLeaveId(), leave.getTypeOfLeave(), leave.getReason(), leave.getDateOfLeave(), 
                		leave.getAppliedDate(), leave.getEmployee().getEmployeeId(), leave.getAvailableLeaves(),leave.getStatus(),leave.getEndDate()))
                .toList();
    }

    
	@Override
	public LeaveBalanceDTO getLeaveBalance(String employeeId) {
	    Employees employee = employeesRepository.findById(employeeId)
	        .orElseThrow(() -> new IllegalArgumentException("Employee not found."));
	    
	    int totalLeaves = employee.getTotalLeaves();
	    int usedLeaveDays = employee.getLeaves().stream()
	        .filter(leave -> APPROVED.equals(leave.getStatus()))
	        .mapToInt(Leaves::getDays)
	        .sum();
	    
	    int availableLeaves = totalLeaves - usedLeaveDays;

	    return new LeaveBalanceDTO(totalLeaves, availableLeaves);
	}



	@Override
	public List<LeaveDTO> employeeOnleave() {
		List<Leaves> pendingLeaves = leavesRepository.findByDateOfLeave(LocalDate.now());
		return pendingLeaves.stream().map(leave -> new LeaveDTO(leave.getLeaveId(),leave.getTypeOfLeave(), leave.getReason(),
				leave.getDateOfLeave(), leave.getAppliedDate(), leave.getEmployee().getEmployeeId(), leave.getAvailableLeaves(),leave.getStatus(),leave.getEndDate()))
				.toList();
	}


	@Override
	public void deleteLeave(Integer leaveId) {
		Leaves leave = leavesRepository.findByLeaveId(leaveId);
	        if (leave != null) {
	            leavesRepository.delete(leave);
	        }
	    }
	
}
