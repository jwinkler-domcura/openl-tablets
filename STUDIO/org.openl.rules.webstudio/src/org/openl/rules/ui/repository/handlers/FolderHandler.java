package org.openl.rules.ui.repository.handlers;

import org.openl.rules.ui.repository.Context;
import org.openl.rules.ui.repository.beans.FolderBean;
import org.openl.rules.ui.repository.beans.FileBean;
import org.openl.rules.ui.repository.beans.AbstractEntityBean;
import org.openl.rules.repository.RFile;
import org.openl.rules.repository.RFolder;
import org.openl.rules.repository.REntity;
import org.openl.rules.repository.exceptions.RModifyException;
import org.openl.rules.repository.exceptions.RRepositoryException;

import java.util.List;
import java.util.LinkedList;

/**
 * Handler for Folder UI Bean.
 *
 * @author Aleh Bykhavets
 *
 */
public class FolderHandler extends BeanHandler {

    public FolderHandler(Context context) {
        super(context);
    }

    /**
     * Gets list of elements (sub folders, files) for a Folder UI Bean.
     * 
     * @param bean folder UI Bean
     * @return list of elements
     */
    public List<AbstractEntityBean> getElements(FolderBean bean) {
        RFolder folder = findRFolder(bean);

        return listElements(folder);
    }

    /**
     * Adds new sub folder to existing folder.
     * 
     * @param bean Folder UI Bean
     * @param newFolderName name of new sub folder
     * @return whether adding was successful
     */
    public boolean addFolder(FolderBean bean, String newFolderName) {
        RFolder folder = findRFolder(bean);
        
        return addFolder(folder, newFolderName); 
    }

    /**
     * Lists elements for a repository folder.
     * 
     * @param folder a repository folder
     * @return list of elements
     */
    protected List<AbstractEntityBean> listElements(RFolder folder) {
        List<AbstractEntityBean> result = new LinkedList<AbstractEntityBean>();

        try {
            // list of sub folders
            for (RFolder subFolder : folder.getFolders()) {
                FolderBean fb = createBean(subFolder);
                result.add(fb);
            }
        } catch (RRepositoryException e) {
            // TODO: log exception
            context.getMessageQueue().addMessage(e);
        }

        try {
            FileHandler fh = context.getFileHandler();

            // list of files
            for (RFile file : folder.getFiles()) {
                FileBean fb = fh.createBean(file);
                result.add(fb);
            }
        } catch (RRepositoryException e) {
            // TODO: log exception
            context.getMessageQueue().addMessage(e);
        }

        return result;
    }
    
    protected boolean addFolder(RFolder folder, String newFolderName) {
        boolean result;
        
        try {
            folder.createFolder(newFolderName);
            result = true;
        } catch (RModifyException e) {
            // TODO: log exception
            context.getMessageQueue().addMessage(e);
            result = false;
        }
        
        return result;
    }
    
    /**
     * Creates Folder UI Bean from a repository folder.
     * 
     * @param folder a repository folder
     * @return new Folder UI Bean
     */
    protected FolderBean createBean(RFolder folder) {
        FolderBean fb = new FolderBean();
        initBean(fb, folder);
        return fb;
    }
    
    private RFolder findRFolder(FolderBean bean) {
        String id = bean.getId();
        REntity entity = getEntityById(id);
//        if (entity == null) ...
        RFolder folder = (RFolder) entity;
        // check class cast exception
        
        return folder;
    }
}
