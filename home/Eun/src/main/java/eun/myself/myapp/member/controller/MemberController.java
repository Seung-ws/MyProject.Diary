package eun.myself.myapp.member.controller;


import java.util.UUID;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
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
		
		return "memberLogin/memberLogin";
	}
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String memberLogin(String remember,String userid,String password,HttpSession session,Model model)	
	{		
		//�������� Ž��
		Member member=memberService.selectMember(userid);
		if(member!=null)
		{
			//DB�� password
			String dbPassword=member.getPassword();
			if(dbPassword!=null)
			{
				//�α��� ����
				if(dbPassword.equals(getHash(password,"SHA256")))
				{
					session.setAttribute("userid", userid);
	
					
					
					
					if(remember!=null)
					{
						//��Ű�����ʿ�
						syslog.getLog(remember);
						syslog.getLog("�ڵ��α���üũ�߽��ϴ�");
					}
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
		return "memberLogin/memberLogin";
	}
	@RequestMapping(value="/login.logout",method=RequestMethod.GET)
	public String memberLogout(String refurl,HttpSession session) {		
		//�����ʱ�ȭ
		session.invalidate();
		syslog.getLog("����� �α׾ƿ�");
		//redirect �� uri ����
		if(refurl!=null)
		{
			return "redirect:"+refurl;	
		}else
		{
			return "home/home";
		}
		
	}
	@RequestMapping(value="/memberInsert",method=RequestMethod.GET)
	public String signUpMember()
	{
		return "memberInsert/memberInsert";
	}
	@RequestMapping(value="/memberInsert",method=RequestMethod.POST)
	public String signUpMember(String userid,String password)
	{		
		Member member=memberService.selectMember(userid);
		if(member==null)
		{
			
			Member newmember =new Member();
			String uid=UUID.randomUUID().toString();
			newmember.setUid(uid);
			newmember.setUsername(userid);
			newmember.setPassword(getHash(password,"SHA256"));
			
			memberService.signUpMember(newmember);
			syslog.getLog("id�� �����Ǿ����ϴ�");
		}else
		{
			syslog.getLog("id�� �����մϴ�");
		}
		
		
		
		return "redirect:/";
	}
	@RequestMapping(value="/memberProfile",method=RequestMethod.GET)
	public String memberProfile(HttpSession session,Model model) {
		String userid=(String)session.getAttribute("userid");
		if(userid!=null)
		{
			Member member=memberService.selectMember(userid);	
			if(member!=null)
			{
				model.addAttribute("username",member.getUsername());
				return"memberProfile/memberProfile";
			}
		}
		return "home/home";
		
		
		
		
	
	}
	
	
	
	
	public String getHash(String str, String hashType)
    {
        String result="";
        try{
            MessageDigest messageDigest=MessageDigest.getInstance(hashType);
            messageDigest.update(str.getBytes("UTF-8"));

            byte[] data=messageDigest.digest();
            int dataLength=data.length;

            for(int i=0;i<dataLength;i++)
            {
                result+=(Integer.toString((data[i]&0xff) + 0x100, 16).substring(1));
                //System.out.println(Integer.toHexString(data[i]&0xff));
            }

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            //hashŸ���� ã�� ������ ��;
            e.printStackTrace();
            return null;
        };
        return result;
    }
}
