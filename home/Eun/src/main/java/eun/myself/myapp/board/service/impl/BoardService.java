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
		//���ü����� ������Ʈ�ϰ� �Խ��� ���̵�� ����
		
	}

	@Override
	public void replyArticle(Board board, BoardUploadFile file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getPassword(int board_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateArticle(Board board) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateArticle(Board board, BoardUploadFile file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Board selectDeleteArticle(int board_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteArticle(int board_id, int reply_Number) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int selectTotalArticleCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int selectTotalArticleCountByCategoryId(int category_id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Board> searchListByContentKeyword(String keyword, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int selectTotalArticleCountByKeyword(String keyword) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
