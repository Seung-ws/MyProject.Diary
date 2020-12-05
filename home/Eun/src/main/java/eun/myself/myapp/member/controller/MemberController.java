package eun.myself.myapp.member.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eun.myself.myapp.member.model.Member;
import eun.myself.myapp.member.service.IMemberService;
import eun.myself.myapp.syslog.SysLog;

@Controller
public class MemberController {
	@Autowired
	IMemberService memberService;
	@Autowired
	SysLog syslog;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String memberLogin() {
		
		return "member/login";
	}
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String memberLogin(String username,String password,HttpSession session,Model model)	
	{
		syslog.getLog("�����:"+username);
		//�������� Ž��
		Member member=memberService.selectMember(username);
		if(member!=null)
		{
			//DB�� password
			String dbPassword=member.getPassword();
			if(dbPassword!=null)
			{
				//�α��� ����
				if(dbPassword.contentEquals(password))
				{
					session.setAttribute("username", username);
					session.setAttribute("uid",member.getUid());
					session.setAttribute("gid", member.getGid());
					syslog.getLog("�α��οϷ�");
					//redirect �� uri ����
					return "redirect:/";
					
				}
				else {
					model.addAttribute("message", "WRONG_PASSWORD");
				}
			}
		}else {
			model.addAttribute("message","USER_NOT_FOUND");
		}
		session.invalidate();					
		return "member/login";
	}
	@RequestMapping(value="/login.logout",method=RequestMethod.GET)
	public String memberLogout(String refurl,HttpSession session) {		
		//�����ʱ�ȭ
		session.invalidate();
		syslog.getLog("����� �α׾ƿ�");
		//redirect �� uri ����
		return "redirect:"+refurl;
	}
	@RequestMapping(value="/signup",method=RequestMethod.GET)
	public String signUpMember()
	{
		return "member/signup";
	}
}
