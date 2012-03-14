package edu.missouri.eldercare.application;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		// layout.setFixed(true);
		layout.addStandaloneView("org.eclipse.rse.ui.view.systemView", true,
				IPageLayout.LEFT, 0.2f, editorArea);
		layout.addStandaloneView(
				"edu.missouri.eldercare.application.views.ElderCareView",
				false, IPageLayout.RIGHT, 0.8f, editorArea);
	}
}
