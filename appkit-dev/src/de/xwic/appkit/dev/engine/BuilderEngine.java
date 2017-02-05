/**
 * 
 */
package de.xwic.appkit.dev.engine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import de.xwic.appkit.dev.engine.model.DataModel;
import de.xwic.appkit.dev.engine.model.EntityModel;

/**
 * @author lippisch
 *
 */
public class BuilderEngine {

	private final Log log = LogFactory.getLog(getClass()) ;
	
	private AppContext context;
	private VelocityEngine ve;

	private Template tplEntity;
	private Template tplEntityInterface;
	private Template tplDAOImpl;
	private Template tplHeader;
	private Template tplDAOInterface;
	private Template tplHbmXml;
	private Template tplEntityXml;
	private Template tplListsetupXml;
	private Template tplEditorXml;

	/**
	 * Construct new BuilderEngine on given context.
	 * @param context
	 */
	public BuilderEngine(AppContext context) {
		this.context = context;
	}
	
	/**
	 * 
	 */
	public void start() {
		
		ve = new VelocityEngine();
		Properties prop = new Properties();
		prop.setProperty("resource.loader", "file, class");
		prop.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		prop.setProperty("file.resource.loader.path", ".");
		prop.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		ve.init(prop);

		tplHeader = ve.getTemplate("tpl/java_header.vtl");
		tplEntity = ve.getTemplate("tpl/entity_impl.vtl");
		tplHbmXml = ve.getTemplate("tpl/hbmxml.vtl");
		tplEntityInterface = ve.getTemplate("tpl/entity_interface.vtl");
		tplDAOImpl = ve.getTemplate("tpl/dao_impl.vtl");
		tplDAOInterface = ve.getTemplate("tpl/dao_interface.vtl");

		tplEntityXml = ve.getTemplate("tpl/entity_xml.vtl");
		tplListsetupXml = ve.getTemplate("tpl/listsetup_xml.vtl");
		tplEditorXml = ve.getTemplate("tpl/editor_xml.vtl");

	}

	/**
	 * 
	 */
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return
	 */
	public String getVersion() {
		return "1";
	}

	/**
	 * @param model
	 */
	public void generateFiles(DataModel model, boolean daoFiles, boolean listSetup, boolean editors) {
		
		log.info("Domain: " + model.getDomainId());
		
		File srcBase = new File(context.getRepositoryRoot(), 
				model.getProjectName() + "/" + 
				context.getSourceFolderName() + "/" + 
				model.getPackageName().replace('.', '/'));

		log.info("Source File Base: " + srcBase.getAbsolutePath());
		
		if (!srcBase.exists()) {
			srcBase.mkdirs();
		}
		
		
		for (EntityModel entity : model.getEntities()) {
			log.info("Processing Entity " + entity.getName());

			// generate the entity class
			
			File entityImplFolder = new File(srcBase, "entities/impl");
			if (!entityImplFolder.exists()) {
				entityImplFolder.mkdirs();
			}
			
			VelocityContext veCtx = new VelocityContext();
			veCtx.put("package", model.getPackageName());
			veCtx.put("model", model);
			veCtx.put("entity", entity);

			if (daoFiles) {
				File entityJavaFile = new File(entityImplFolder, entity.getName() + ".java");
				generateFile(entityJavaFile, veCtx, tplEntity);
				
				File hbmXmlFile = new File(entityImplFolder, entity.getName() + ".hbm.xml");
				generateFile(hbmXmlFile, veCtx, tplHbmXml, false);
				
				// generate the interface file
				File entityFolder = new File(srcBase, "entities");
				// it must already exist as it is simply the parent of the implementation folder
				File entityInterfaceJavaFile = new File(entityFolder, "I" + entity.getName() + ".java");
				generateFile(entityInterfaceJavaFile, veCtx, tplEntityInterface);
	
				// generate the DAO implementation file
				File daoImplFolder = new File(srcBase, "dao/impl");
				if (!daoImplFolder.exists()) {
					daoImplFolder.mkdirs();
				}
				File daoImplJavaFile = new File(daoImplFolder, entity.getName() + "DAO.java");
				generateFile(daoImplJavaFile, veCtx, tplDAOImpl);
	
				// and the DAO interface
				File daoFolder = new File(srcBase, "dao");
				File daoJavaFile = new File(daoFolder, "I" + entity.getName() + "DAO.java");
				generateFile(daoJavaFile, veCtx, tplDAOInterface);
			}
			
			
			// add the entity.xml in the product configuration
			if (!context.getProductConfigFolder().exists()) {
				context.getProductConfigFolder().mkdirs();
			}
			File domainFolder = new File(context.getProductConfigFolder(), model.getDomainId());
			domainFolder.mkdirs();

			// entities xml file
			File cfgEntities = new File(domainFolder, "entities");
			cfgEntities.mkdirs();
			File entityXmlFile = new File(cfgEntities, entity.getName() + ".xml");
			generateFile(entityXmlFile, veCtx, tplEntityXml,false);

			if (listSetup) {
				// listsetup
				File cfgListsetup = new File(domainFolder, "listsetup");
				cfgListsetup.mkdirs();
				File entityListetupFile = new File(cfgListsetup, entity.getName() + "-default.list.xml");
				generateFile(entityListetupFile, veCtx, tplListsetupXml,false);
			}

			if (editors) {
				// editor
				File cfgEditors = new File(domainFolder, "editors");
				cfgEditors.mkdirs();
				File editorFile = new File(cfgEditors, entity.getName() + "-default.editor.xml");
				generateFile(editorFile, veCtx, tplEditorXml,false);
			}
			
		}
		
		
		
	}

	private void generateFile(File javaFile, VelocityContext veCtx, Template template) {
		generateFile(javaFile, veCtx, template, true);
	}

	/**
	 * @param javaFile
	 * @param veCtx
	 * @param tplEntity2
	 */
	private void generateFile(File javaFile, VelocityContext veCtx, Template template, boolean includeHeader) {
		
		try {
			FileWriter fw = new FileWriter(javaFile, false);
			if (includeHeader) {
				tplHeader.merge(veCtx, fw);
			}
			template.merge(veCtx, fw);
			fw.flush();
			fw.close();
		} catch (IOException ioe) {
			log.error("Error writing template", ioe);
		}

	}
	
}
