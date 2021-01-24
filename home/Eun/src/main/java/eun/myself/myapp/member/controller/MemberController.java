package eun.myself.myapp.member.controller;


import java.util.UUID;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.Timestamp;

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
	
	@RequestMapping(value = "/memberLogin", method = RequestMethod.GET)
	public String memberLogin(HttpSession session) {
		String user_id=(String)session.getAttribute("user_id");
		syslog.getLog("GET-login ���� & ���� Ȯ��");
		if(user_id!=null)
		{
			syslog.getLog("GET-login -> /memberProfile");
			return "redirect:/memberProfile";
		}
		else
		{
			syslog.getLog("GET-login ���� ���� ->/memberlogin");
			return "memberLogin/memberLogin";	
		}		
	}
	
	@RequestMapping(value="/memberLogin",method=RequestMethod.POST)
	public String memberLogin(String remember,String user_id,String user_password,HttpSession session,Model model)	
	{		
		syslog.getLog("GET->memberLogin ����");
		//�������� Ž��
		Member member=memberService.selectMember(user_id);
		if(member!=null)
		{			
			//DB�� password
			String dbPassword=member.getUser_password();
			if(dbPassword!=null)
			{
				//�α��� ����
				if(dbPassword.equals(getHash(user_password,"SHA256")))
				{
					session.setAttribute("user_id", member.getUser_id());
					session.setAttribute("user_name",member.getUser_name());
	
					if(remember!=null)
					{
						//��Ű�����ʿ�
						syslog.getLog(remember);
						syslog.getLog("�ڵ��α���üũ�߽��ϴ�");
					}
					syslog.getLog("�α��οϷ�");
					
					//redirect �� uri ����
					syslog.getLog("GET->memberLogin redirect/ ������");
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
		syslog.getLog("GET->memberLogin ���� �ٽ� ����");
		return "memberLogin/memberLogin";
	}
	@RequestMapping(value="/memberLogout",method=RequestMethod.GET)
	public String memberLogout(String refurl,HttpSession session) {
		syslog.getLog("GET->memberLogout ����");
		//�����ʱ�ȭ
		session.invalidate();		
		//redirect �� uri ����
		syslog.getLog("GET->memberLogout �α׾ƿ�����");
		if(refurl!=null)
		{
			syslog.getLog("GET->memberLogout refurl�� ������");
			return "redirect:"+refurl;	
		}else
		{
			syslog.getLog("GET->memberLogout home���� ������");
			return "home/home";
		}		
	}
	
	@RequestMapping(value="/memberInsert",method=RequestMethod.GET)
	public String signUpMember()
	{
		return "memberInsert/memberInsert";
	}
	@RequestMapping(value="/memberInsert",method=RequestMethod.POST)
	public String signUpMember(String user_id,String user_email,String user_password)
	{		
		Member member=memberService.selectMember(user_id);
		if(member==null)
		{
			
			Member newmember =new Member();
			String user_uid=UUID.randomUUID().toString();
			newmember.setUser_uid(user_uid);
			newmember.setUser_gid(user_uid);
		//	syslog.getLog(user_uid);
			newmember.setUser_id(user_id);
		//	syslog.getLog(user_id);
			newmember.setUser_name(user_id);			
			newmember.setUser_email(user_email);
			//syslog.getLog(user_email);
			newmember.setUser_password(getHash(user_password,"SHA256"));
		//	syslog.getLog(user_password);
			
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
		String user_id=(String)session.getAttribute("user_id");
		
		if(user_id!=null)
		{
			Member member=memberService.selectMember(user_id);	
			if(member!=null)
			{
				model.addAttribute("member",member);			
				return"memberProfile/memberProfile";
			}
		}
		return "redirect:/login";
	}
	
	@RequestMapping(value="/memberProfile",method=RequestMethod.POST)
	public String memberProfile(HttpSession session,String user_name,String user_email,String user_password)
	{
		syslog.getLog("memberProfile Update ����");
		String user_id=(String)session.getAttribute("user_id");
		if(user_id!=null)
		{
			Member member=memberService.selectMember(user_id);
			if(member!=null)
			{
				
				if(user_name!=null&&user_name!="")member.setUser_name(user_name);
				if(user_email!=null&&user_email!="")member.setUser_email(user_email);
				if(user_password!=null&&user_password!="")member.setUser_password(getHash(user_password,"SHA256"));
				syslog.getLog("memberProfile Update ����");	
				
				boolean updatelog=memberService.memberUpdate(member);
				if(updatelog)
				{
					syslog.getLog("memberProfile Update ����");	
					//������Ʈ ����
					return "redirect:/memberProfile";
				}
				syslog.getLog("memberProfile Update ����");
				//������Ʈ ����
			}
			
		}
		return "redirect:/memberProfile";
	}
	
	@RequestMapping(value="/memberDelete",method=RequestMethod.POST)
	public String memberDelete(HttpSession session) {
		String user_id=(String)session.getAttribute("user_id");
		if(user_id !=null)
		{
			syslog.getLog("POST->memberDelete ���� Ȯ��");
			try{
				memberService.memberDelete(user_id);
				session.invalidate();
				syslog.getLog("POST->memberDelete ����");
				return "redirect:/";
			}catch(Exception e)
			{
				syslog.getLog("POST->memberDelete ���� ����");				
			}
		}
		syslog.getLog("POST->memberDelete ����");
		return "redirect:/";
		
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
