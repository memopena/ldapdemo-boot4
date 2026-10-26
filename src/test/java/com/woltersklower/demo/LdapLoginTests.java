package com.woltersklower.demo;

import static org.hamcrest.Matchers.endsWith;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class LdapLoginTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void adminLogsInAgainstEmbeddedLdap() throws Exception {
        mvc.perform(formLogin("/doLogin").user("user2").password("memo"))
            .andExpect(authenticated().withUsername("user2").withRoles("ADMIN"));
    }

    @Test
    void regularUserGetsUserRole() throws Exception {
        mvc.perform(formLogin("/doLogin").user("user1").password("memo"))
            .andExpect(authenticated().withUsername("user1").withRoles("USER"));
    }

    @Test
    void wrongPasswordIsRejected() throws Exception {
        mvc.perform(formLogin("/doLogin").user("user1").password("wrong"))
            .andExpect(unauthenticated());
    }

    @Test
    void anonymousIsRedirectedToLogin() throws Exception {
        mvc.perform(get("/user"))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", endsWith("/login")));
    }

    @Test
    void userRoleCanListUsers() throws Exception {
        mvc.perform(get("/user").with(user("user1").roles("USER")))
            .andExpect(status().isOk());
    }

    @Test
    void userRoleCannotOpenUserDetails() throws Exception {
        mvc.perform(get("/user/1").with(user("user1").roles("USER")))
            .andExpect(status().isForbidden());
    }

}
