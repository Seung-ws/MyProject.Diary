package eun.myself.myapp.board.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eun.myself.myapp.board.dao.IBoardRepository;
import eun.myself.myapp.board.model.Board;
import eun.myself.myapp.board.model.BoardUploadFile;
import eun.myself.myapp.board.service.IBoardService;

@Service
public class BoardService implements IBoardService{

	@Autowired
	@Qualifier("IBoardRepository")
	IBoardRepository boardRepository;
	
	//���ÿ� ó�� �ɶ� ���� �߻��� ���� ó��
	@Transactional
	public void insertArticle(Board board) {
		// ������ ������ �����ؼ� ���̵� ��ġ�� �ʰ� �����ϰ� �����Ѵ�.
		board.setBoard_id(boardRepository.selectMaxArticleNo()+1);
		boardRepository.insertArticle(board);
	}

	//���ÿ� ó�� �ɶ� ���� �߻��� ���� ó��
	@Transactional
	public void insertArticle(Board board, BoardUploadFile file) {
		// ������ ������ �����ؼ� ���̵� ��ġ�� �ʰ� �����ϰ� �����Ѵ�.
		board.setBoard_id(boardRepository.selectMaxArticleNo()+1);
		boardRepository.insertArticle(board);
	}

	@Override
	public List<Board> selectArticleListByCategory(int category_id, int page) {
		//
		int start=(page-1)*10;
		//���� �������� ����ϰ� �Խ��ǿ��� �ش� ������ ī�װ� �Խù��� 10���� ������ �´�. ������ �´�.
		return boardRepository.selectArticleListByCategory(category_id, start, start+10);
	}

	@Override
	public List<Board> selectArticleListByCategory(int category_id) {
		// TODO Auto-generated method stub
		//������ �������� �������� �ʾ��� �� 100�� ���� �Խù� �������� ������ �´�.
		return boardRepository.selectArticleListByCategory(category_id, 0, 100);
	}
	@Transactional
	public Board selectArticle(int board_id) {
		//�ش� �Խù��� Ȯ�ν� ��ȸ���� �����ϰ� �ϰ� �Խù��� ������ �´�.
		boardRepository.updateReadCount(board_id);
		return boardRepository.selectArticle(board_id);
	}

	@Override
	public BoardUploadFile getFile(int file_id) {
		//�Խù��� ������ �ִٸ� ������ ������ �Ѵ�.
		return boardRepository.getFile(file_id);
	}

	@Transactional
	public void replyArticle(Board board) {
		//��� ������ ������Ʈ�ϰ� ������ ���� ��� �ø��� 
		
		if(board.getReply_step()>0)
		{
			int sum =boardRepository.selectMaxSameStep(board.getMaster_id(),
					board.getReply_step()+1, board.getReply_number());
			int reply_no=boardRepository.selectCustomMaxReplyNo(board.getMaster_id(),
					board.getReply_step()+1, board.getReply_number());
			System.out.println("Ȯ�ο� sum : "+sum);
			System.out.println("Ȯ�ο� reply_no : "+reply_no);
			System.out.println("Ȯ�ο� getReply_number : "+board.getReply_number());
			
			boardRepository.updateReplyNumber(board.getMaster_id(), board.getReply_number()+sum);	
			board.setBoard_id(boardRepository.selectMaxArticleNo()+1);
			board.setReply_parents_number(board.getReply_number());
			board.setReply_number(board.getReply_number()+1+sum);
			board.setReply_step(board.getReply_step()+1);	
		
		}
		else {
			//����� ���� �Խù��� �� ���Ź��
			board.setBoard_id(boardRepository.selectMaxArticleNo()+1);
			board.setReply_parents_number(board.getReply_number());
			board.setReply_number(boardRepository.selectMaxReplyNo(board.getMaster_id())+1);
			board.setReply_step(board.getReply_step()+1);	
		}
		
		
		boardRepository.replyArticle(board);
	}

	@Transactional
	public void replyArticle(Board board, BoardUploadFile file) {
		//��� ������ ������Ʈ�ϰ� ������ ���� ��� �ø��� 
		boardRepository.updateReplyNumber(board.getMaster_id(), board.getReply_number());	
		board.setBoard_id(boardRepository.selectMaxArticleNo()+1);
		board.setReply_number(board.getReply_number()+1);
		board.setReply_step(board.getReply_step()+1);
		boardRepository.replyArticle(board);
		if(file != null && file.getFile_name() != null && !file.getFile_name().equals("")) {
        	file.setBoard_id(board.getBoard_id());
        	boardRepository.insertFileData(file);
        }
		
	}

	@Override
	public String getPassword(int board_id) {
		// �Խù� ��й�ȣ�� �����´�
		return boardRepository.getPassword(board_id);
	}

	@Override
	public void updateArticle(Board board) {
		// �Խù��� ������Ʈ �Ѵ�. ���Ͼ���
		boardRepository.updateArticle(board);
	}

	@Transactional
	public void updateArticle(Board board, BoardUploadFile file) {
		// �Խù��� ������Ʈ�Ѵ�. ���� ����
		boardRepository.updateArticle(board);
        if(file != null && file.getFile_name() != null && !file.getFile_name().equals("")) {
        	file.setBoard_id(board.getBoard_id());
//        	System.out.println(file.toString());
        	if(file.getFile_id()>0) {
        		boardRepository.updateFileData(file);
        	}else {
        		boardRepository.insertFileData(file);
        	}
        }
	}

	@Override
	public Board selectDeleteArticle(int board_id) {
		// �Խù��� �����. 
		System.out.println("���ðԽù������");
		
		return boardRepository.selectDeleteArticle(board_id);
	}

	@Transactional
	public void deleteArticle(int board_id,int master_id, int reply_Number) {
		// �Խù��� ����� ��۵� ����� ������ �Խù��� �����.
		System.out.println("�Խù������");
		if(reply_Number>0) {
			System.out.println("master_id : "+master_id);
			System.out.println("reply_Number : "+reply_Number);
			
			boardRepository.test(master_id,reply_Number);
			boardRepository.deleteReplyFileData(board_id);
			boardRepository.deleteArticleByBoardId(board_id);
			
		}else if(reply_Number==0){
			boardRepository.deleteFileData(board_id);
			boardRepository.deleteArticleByMasterId(board_id);
		}else {
			throw new RuntimeException("WRONG_REPLYNUMBER");
		}
	}

	@Override
	public int selectTotalArticleCount() {
		// ��ü �Խù��� ���ڸ� ��ȯ�Ѵ�.
		return boardRepository.selectTotalArticleCount();
	}

	@Override
	public int selectTotalArticleCountByCategoryId(int category_id) {
		// Ư�� ī�װ��� �Խù� ���ڸ� ��ȯ�Ѵ�.
		return boardRepository.selectTotalArticleCountByCategoryId(category_id);
	}

	@Override
	public List<Board> searchListByContentKeyword(String keyword, int page) {
		// Ű���� �˻� �� �ش� ������������ŭ ��ȯ�Ѵ�.
		int start = (page-1) * 10;
		return boardRepository.searchListByContentKeyword("%"+keyword+"%", start, start+10);
	}

	@Override
	public int selectTotalArticleCountByKeyword(String keyword) {
		// Ű���� �˻��� �Խù� ���� ��ȯ�Ѵ�.
		return boardRepository.selectTotalArticleCountByKeyword("%"+keyword+"%");
	}
	
	
}
