package eun.myself.myapp.board.model;

import java.sql.Timestamp;

import org.springframework.web.multipart.MultipartFile;

public class Board {
	//�Խ���, ī�װ� 
	private int board_id;
	private int category_id;	
	//��, ���̵�
	private String writer;
	private String writer_id;
	//�Խù� ��� ����, ����, �ð�
	private String board_password;
	private String title;
	private String content;
	private Timestamp writeDate;
	//�Խù� ���� ���� ����
	private int master_id;
	private int read_count;
	private int reply_number;
	private int reply_step;
	private int seq;
	private int page;
	private BoardCategory category;
	//��������
	private MultipartFile file;
	private String file_id;
	private String file_name;
	private long file_size;
	private String file_content_type;
	
	
	
	
}
