package eun.myself.myapp.board.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eun.myself.myapp.board.dao.IBoardCategoryRepository;
import eun.myself.myapp.board.model.BoardCategory;
import eun.myself.myapp.board.service.IBoardCategoryService;

@Service
public class BoardCategoryService implements IBoardCategoryService{

	@Autowired
	IBoardCategoryRepository boardCategoryRepository;
	
	@Override
	public List<BoardCategory> selectAllCategory() {
		// ��ü ī�װ� ����Ʈ�� �ҷ��´�.
		return boardCategoryRepository.selectAllCategory();
	}

	@Override
	public List<BoardCategory> selectAllCategoryByClass1(int class1) {
		// ��ü ī�װ�����Ʈ�� ???
		return boardCategoryRepository.selectAllCategoryByClass1(class1);
	}

	@Override
	public void insertNewCategory(BoardCategory boardCategory) {
		// ���ο� ī�װ��� �����Ѵ�.
		boardCategory.setCategory_id(boardCategoryRepository.selectMaxCategoryId());
		boardCategoryRepository.insertNewCategory(boardCategory);
		
	}

	@Override
	public void updateCategory(BoardCategory boardCategory) {
		//ī�װ� ������ ������Ʈ �Ѵ�.
		boardCategoryRepository.updateCategory(boardCategory);
	}

	@Override
	public void deleteCategory(int category_id) {
		// ī�װ��� �����Ѵ�.
		boardCategoryRepository.deleteCategory(category_id);
	}
	

}
