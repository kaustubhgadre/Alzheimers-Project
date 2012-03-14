package eldercare.rap;

import org.eclipse.rwt.SessionSingletonBase;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IViewPart;

public class MySessionSingleton extends SessionSingletonBase {
	private MySessionSingleton() {
	}

	public static MySessionSingleton getInstance() {
		return (MySessionSingleton) getInstance(MySessionSingleton.class);
	}

	public void showData(IViewPart elderCareView, int[] finalData) {
		if (elderCareView instanceof View) {
			Label[] labels = ((View) elderCareView).getChildren();
			for (int i = 0; i < finalData.length; i++) {
				if (finalData[i] == 1) {
					labels[i].setBackground(new Color(null, 237, 28, 36));
					Composite parent = labels[i].getParent();
					if (parent instanceof Canvas) {
						Canvas canvas = (Canvas) parent;
						canvas.setBackground(new Color(null, 237, 28, 36));
						int count = Integer.parseInt(labels[i].getText()) + 1;
						labels[i].setText("" + count);

					}
				} else {
					Color backGroundColor = (Color) labels[i]
							.getData("greenBack");
					labels[i].setBackground(backGroundColor);
					Composite parent = labels[i].getParent();
					if (parent instanceof Canvas) {
						Canvas canvas = (Canvas) parent;
						canvas.setBackground(backGroundColor);
					}
				}
			}
			((View) elderCareView).redraw();
		}

	}

	// add other methods ...
}