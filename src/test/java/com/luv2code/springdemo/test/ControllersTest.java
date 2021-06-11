package com.luv2code.springdemo.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.bind.support.SessionStatus;

import com.luv2code.springdemo.controller.CustomerController;
import com.luv2code.springdemo.entity.Customer;
import com.luv2code.springdemo.service.CustomerService;

public class ControllersTest {

	private CustomerController customerController;

	@Mock
	CustomerService customerService;

	@Mock
	Model model;

	@Mock
	SessionStatus sessionStatus;

	public ControllersTest() {
		MockitoAnnotations.initMocks(this);
		customerController = new CustomerController(customerService);
	}

	@Test
	public void testCustomerController() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(customerController)
				.build();

		Customer customer2 = Customer.builder().id("2").firstName("testF2")
				.lastName("testL2").email("test2@test.com").build();
		List<Customer> customerList = List.of(
				Customer.builder().id("1").firstName("testF1").lastName("testL1").email("test1@test.com").build(),
				customer2
				);


		when(customerService.getCustomers()).thenReturn(customerList);
		ArgumentCaptor<List<Customer>> argumentCaptorCustomerList = ArgumentCaptor
				.forClass(List.class);
		// test once by using springs mockMvc
		mockMvc.perform(get("/customer/list"))
				.andExpect(status().isOk())
				.andExpect(view().name("list-customers"));
		// test by passing mocked model
		customerController.listCustomers(model);
		verify(customerService, times(2)).getCustomers();
		verify(model, times(1)).addAttribute(eq("customers"),
				argumentCaptorCustomerList.capture());
		List<Customer> customersFromModel = argumentCaptorCustomerList
				.getValue();
		assertTrue(customersFromModel.size() == 2);
		assertTrue(customersFromModel.get(0).getFirstName().contains("testF"));

		// test customer add
		String view = customerController.showFormForAdd(model);
		assertEquals(view, "customer-form");
		verify(model, times(1)).addAttribute(eq("customer"),
				any(Customer.class));
		clearInvocations(model);

		// test customer save
		Customer customerToSave = Customer.builder().id("3").firstName("testF3")
				.lastName("testL3").email("test3@test.com").build();
		view = customerController.saveCustomer(customerToSave, sessionStatus);
		verify(customerService, times(1))
				.saveCustomer(any(Customer.class));
		verify(sessionStatus, times(1)).setComplete();
		assertEquals(view, "redirect:/customer/list");

		// test customer update
		ArgumentCaptor<Customer> argumentCaptorCustomer = ArgumentCaptor
				.forClass(Customer.class);
		when(customerService.getCustomer(customer2.getId())).thenReturn(customer2);
		view=customerController.showFormForUpdate("2", model);
		verify(customerService, times(1)).getCustomer(eq(customer2.getId()));
		verify(model, times(1)).addAttribute(eq("customer"),
				argumentCaptorCustomer.capture());
		Customer addedCustomer = argumentCaptorCustomer.getValue();
		assertEquals(addedCustomer.getFirstName(), customer2.getFirstName());
		assertEquals(view, "customer-form");
		clearInvocations(model);

		// test customer delete
		view = customerController.deleteCustomer(customer2.getId());
		verify(customerService, times(1)).deleteCustomer(customer2.getId());
		assertEquals(view, "redirect:/customer/list");
	}

}
