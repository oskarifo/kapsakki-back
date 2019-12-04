package fi.forsblom.kapsakki.list;

import fi.forsblom.kapsakki.exceptions.DatabaseException;

public interface IListService {	
	public List createList(List list, String ownerId) throws DatabaseException;
	public java.util.List<List> getLists(String ownerId) throws DatabaseException;
	public List getList(int listId, String ownerId) throws DatabaseException;
	public List editList(List list, String ownerId) throws DatabaseException;
	public int deleteList(int listId, String ownerId) throws DatabaseException;
}
