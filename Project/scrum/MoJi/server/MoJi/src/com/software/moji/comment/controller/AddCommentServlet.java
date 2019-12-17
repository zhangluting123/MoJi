package com.software.moji.comment.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.software.moji.comment.service.CommentService;
import com.software.moji.entity.Mail;
import com.software.moji.entity.Note;
import com.software.moji.entity.User;
import com.software.moji.mail.dao.MailDao;
import com.software.moji.note.dao.NoteDao;
import com.software.moji.user.dao.UserDao;
import com.software.moji.util.JPushUtil;

/**
 * Servlet implementation class AddCommentServlet
 */
@WebServlet("/AddCommentServlet")
public class AddCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddCommentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		String noteId = request.getParameter("noteId");
		String userId = request.getParameter("userId");
		String commentContent = request.getParameter("commentContent");
		
		Date date = new Date();
		//�����ڸ�ʽ��
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddmmss");
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
		//ת�����ַ�����ʽ
		String commentId = simpleDateFormat.format(date) + noteId;
		String commentTime = simpleDateFormat1.format(date);
		
		PrintWriter writer = response.getWriter();
		CommentService commentService = new CommentService();
		
		int i = commentService.addComment(commentId, noteId, userId, commentContent, commentTime);
		if(i > 0) {
			writer.write("1");
			System.out.println("�������ӳɹ�");
			Note note = null;
			User user = null;
			try {
				note = new NoteDao().queryUserId(noteId);//�յ����۵���
				user = new UserDao().queryUser(userId);//�������۵���
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Map<String, String> extrasparams = new HashMap<>();
			extrasparams.put("acceptTime", commentTime);
			extrasparams.put("myName", note.getUser().getUserName());
			extrasparams.put("otherName", user.getUserName());
			extrasparams.put("commentContent", commentContent);
			System.out.println("userId="+userId+";notegetuserid="+note.getUser().getUserId());
			int j = JPushUtil.sendToBieMing(note.getUser().getUserId(),"����~","����Ϣ",user.getUserName()+"��������,��ȥ�����ɣ�",extrasparams);
			if(j == 1) {
				writer.write("2");
				System.out.println("[�����ѷ���֪ͨ]");
				Mail mail = new Mail();
				mail.setAcceptTime(commentTime);
				mail.setCommentContent(commentContent);
				mail.setOtherName(user.getUserName());
				mail.setUserId(note.getUser().getUserId());
				try {
					boolean b = new MailDao().insertMail(mail);
					if(b) {
						System.out.println("֪ͨ��Ϣд��ɹ���");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}else {
			writer.write("0");
			System.out.println("��������ʧ��");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}