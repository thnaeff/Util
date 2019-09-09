/**
 * Copyright 2014 Thomas Naeff (github.com/thnaeff)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * 
 */
package ch.thn.util.gui.test;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import ch.thn.util.gui.Loader;
import ch.thn.util.gui.component.imageanimation.ImageAnimationLabelFading;
import ch.thn.util.gui.component.imageanimation.ImageAnimationLabelRotating;
import ch.thn.util.gui.component.imageanimation.ImageAnimationLabelSwapping;
import ch.thn.util.gui.effects.imageanimation.ImageAnimationFading;
import ch.thn.util.gui.effects.imageanimation.ImageAnimationRotating;
import ch.thn.util.gui.effects.imageanimation.ImageAnimationSwapping;
import ch.thn.util.thread.ControlledRunnable;
import ch.thn.util.thread.ControlledRunnable.ControlledRunnableEvent;
import ch.thn.util.thread.ControlledRunnable.ControlledRunnableListener;
import ch.thn.util.valuerange.ImageAlphaGradient;

/**
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class EffectsTest extends JPanel implements ControlledRunnableListener, ActionListener {
  private static final long serialVersionUID = 8762214384151119341L;

  private ImageAnimationLabelFading lImageAnimationLabelFading = null;
  private ImageAnimationLabelRotating lImageAnimationLabelRotating = null;
  private ImageAnimationLabelSwapping lImageAnimationLabelSwapping = null;
  private JLabel lImageAnimationFading = null;
  private JLabel lImageAnimationRotating = null;
  private JLabel lImageAnimationSwapping = null;

  private JButton bPauseRun = null;
  private JButton bStop = null;
  private JButton bReset = null;

  private ImageAnimationFading imageAnimationFading = null;
  private ImageAnimationRotating imageAnimationRotating = null;
  private ImageAnimationSwapping imageAnimationSwapping = null;


  private static final ImageIcon[] icons =
      {Loader.loadIcon("/16x16/close_hover.png"), Loader.loadIcon("/16x16/close_pressed.png"),
          Loader.loadIcon("/16x16/close.png"), Loader.loadIcon("/16x16/folder_explore.png"),
          Loader.loadIcon("/16x16/folder.png"), Loader.loadIcon("/16x16/question.png"),};

  /**
   * 
   */
  public EffectsTest() {

    setLayout(new FlowLayout(FlowLayout.LEFT));

    bPauseRun = new JButton("Pause");
    bPauseRun.addActionListener(this);

    bStop = new JButton("Stop");
    bStop.addActionListener(this);

    bReset = new JButton("Reset");
    bReset.addActionListener(this);

    add(bStop);
    add(bReset);
    add(bPauseRun);


    // ================================

    ImageAlphaGradient simpleGradientIn =
        new ImageAlphaGradient(ImageAlphaGradient.FADE_IN, 20, 0, 0);
    ImageAlphaGradient simpleGradientOut =
        new ImageAlphaGradient(ImageAlphaGradient.FADE_OUT, 20, 0, 0);

    System.out.println(simpleGradientIn);
    System.out.println(simpleGradientOut);

    lImageAnimationFading = new JLabel(ImageAnimationFading.class.getSimpleName()
        + ":");
    lImageAnimationFading.setHorizontalTextPosition(SwingConstants.LEFT);

    imageAnimationFading = new ImageAnimationFading(lImageAnimationFading, 40, 40);
    imageAnimationFading.addControlledRunnableListener(this);

    imageAnimationFading.addStep(icons[0].getImage(), simpleGradientIn, 80, 2000, 0);
    imageAnimationFading.addStep(icons[0].getImage(), simpleGradientOut, 80, 1000, 0);

    lImageAnimationFading.setIcon(new ImageIcon(imageAnimationFading.getOutputImage()));


    Thread tFading = new Thread(imageAnimationFading);
    tFading.start();

    imageAnimationFading.go(2, false);


    // ================================


    lImageAnimationLabelFading =
        new ImageAnimationLabelFading(ImageAnimationLabelFading.class.getSimpleName()
            + ":");
    lImageAnimationLabelFading.setHorizontalTextPosition(SwingConstants.LEFT);
    lImageAnimationLabelFading.getImageAnimation().addControlledRunnableListener(this);

    lImageAnimationLabelFading.setIcon(icons[5]);
    lImageAnimationLabelFading.addStep(new ImageAlphaGradient(ImageAlphaGradient.FADE_OUT, 20), 100,
        0, 0);
    lImageAnimationLabelFading.addStep(new ImageAlphaGradient(ImageAlphaGradient.FADE_IN, 5), 500,
        0, 0);
    lImageAnimationLabelFading.addStep(new ImageAlphaGradient(ImageAlphaGradient.FADE_OUT, 2), 500,
        0, 5);



    lImageAnimationLabelFading.animate(3);

    lImageAnimationLabelFading.setIcon(icons[4]);
    lImageAnimationLabelFading.animate(3);


    // ================================

    lImageAnimationRotating = new JLabel(ImageAnimationRotating.class.getSimpleName()
        + ":");
    lImageAnimationRotating.setHorizontalTextPosition(SwingConstants.LEFT);

    imageAnimationRotating = new ImageAnimationRotating(lImageAnimationRotating, 20, 20);
    imageAnimationRotating.addControlledRunnableListener(this);

    imageAnimationRotating.addStep(icons[5].getImage(), 180, 10, 50, 500, 0);
    imageAnimationRotating.addStep(icons[5].getImage(), 180, -10, 50, 500, 0);
    imageAnimationRotating.addStep(icons[5].getImage(), 180, 10, 50, 500, 0);

    lImageAnimationRotating.setIcon(new ImageIcon(imageAnimationRotating.getOutputImage()));


    Thread tRotating = new Thread(imageAnimationRotating);
    tRotating.start();

    imageAnimationRotating.go(3, false);

    // ================================


    lImageAnimationLabelRotating =
        new ImageAnimationLabelRotating(ImageAnimationLabelRotating.class.getSimpleName()
            + ":");
    lImageAnimationLabelRotating.setHorizontalTextPosition(SwingConstants.LEFT);
    lImageAnimationLabelRotating.getImageAnimation().addControlledRunnableListener(this);

    lImageAnimationLabelRotating.setIcon(icons[5]);
    lImageAnimationLabelRotating.addStep(360, 5, 50, 0, 0);
    lImageAnimationLabelRotating.addStep(360, -5, 50, 0, 0);



    lImageAnimationLabelRotating.animate(3);

    lImageAnimationLabelRotating.setIcon(icons[4]);
    lImageAnimationLabelRotating.animate(3);

    // ================================

    lImageAnimationSwapping = new JLabel(ImageAnimationSwapping.class.getSimpleName()
        + ":");
    lImageAnimationSwapping.setHorizontalTextPosition(SwingConstants.LEFT);

    imageAnimationSwapping = new ImageAnimationSwapping(lImageAnimationSwapping, 20, 20);
    imageAnimationSwapping.addControlledRunnableListener(this);

    imageAnimationSwapping.addStep(icons[0].getImage(), 0, 500);
    imageAnimationSwapping.addStep(icons[1].getImage(), 0, 500);
    imageAnimationSwapping.addStep(icons[2].getImage(), 0, 500);

    lImageAnimationSwapping.setIcon(new ImageIcon(imageAnimationSwapping.getOutputImage()));


    Thread tSwapping = new Thread(imageAnimationSwapping);
    tSwapping.start();

    imageAnimationSwapping.go(3, false);


    // ================================

    lImageAnimationLabelSwapping =
        new ImageAnimationLabelSwapping(ImageAnimationLabelSwapping.class.getSimpleName()
            + ":");
    lImageAnimationLabelSwapping.setHorizontalTextPosition(SwingConstants.LEFT);
    lImageAnimationLabelSwapping.getImageAnimation().addControlledRunnableListener(this);

    lImageAnimationLabelSwapping.addStep(icons[0].getImage(), 0, 2000);
    lImageAnimationLabelSwapping.addStep(icons[1].getImage(), 0, 500);
    lImageAnimationLabelSwapping.addStep(icons[2].getImage(), 1000, 500);

    lImageAnimationLabelSwapping.setIcon(icons[5]);

    lImageAnimationLabelSwapping.animate(3);


    // ================================

    add(lImageAnimationFading);
    // add(lImageAnimationLabelFading);
    // add(lImageAnimationRotating);
    add(lImageAnimationLabelRotating);
    // add(lImageAnimationSwapping);
    add(lImageAnimationLabelSwapping);

  }


  // @Override
  // public void paint(Graphics g) {
  // super.paint(g);
  // g.drawImage(image2, 80, 80, null);
  // }

  @Override
  public void runnableStateChanged(ControlledRunnableEvent e) {
    ControlledRunnable cr = (ControlledRunnable) e.getSource();


    if (cr == imageAnimationFading) {
      switch (e.getStateType()) {
        case RUNNING:
          System.out.print(imageAnimationFading.getClass().getSimpleName()
              + ": ");
          System.out.println("running="
              + cr.isRunning()
              + ", stopped="
              + cr.isStopped()
              + ", will stop="
              + cr.willStop());
          break;
        case PAUSED:
          if (imageAnimationFading.isAnimating()) {
            bPauseRun.setText("Pause");
          } else {
            bPauseRun.setText("Run");
          }
          System.out.print(imageAnimationFading.getClass().getSimpleName()
              + ": ");
          System.out.println("paused="
              + cr.isPaused());
          break;
        case RESET:
          System.out.print(imageAnimationFading.getClass().getSimpleName()
              + ": ");
          System.out.println("will reset="
              + cr.willReset());
          break;
        case WAIT:
          break;
        default:
          System.out.print(imageAnimationFading.getClass().getSimpleName()
              + ": ");
          System.out.println("unknown");
          break;
      }
    } else if (cr == lImageAnimationLabelFading.getImageAnimation()) {
      switch (e.getStateType()) {
        case RUNNING:
          System.out.print(lImageAnimationLabelFading.getClass().getSimpleName()
              + ": ");
          System.out.println("running="
              + cr.isRunning()
              + ", stopped="
              + cr.isStopped()
              + ", will stop="
              + cr.willStop());
          break;
        case PAUSED:
          System.out.print(lImageAnimationLabelFading.getClass().getSimpleName()
              + ": ");
          System.out.println("paused="
              + cr.isPaused());
          break;
        case RESET:
          System.out.print(lImageAnimationLabelFading.getClass().getSimpleName()
              + ": ");
          System.out.println("will reset="
              + cr.willReset());
          break;
        case WAIT:
          break;
        default:
          System.out.print(lImageAnimationLabelFading.getClass().getSimpleName()
              + ": ");
          System.out.println("unknown");
          break;
      }
    } else if (cr == imageAnimationRotating) {
      switch (e.getStateType()) {
        case RUNNING:
          System.out.print(imageAnimationRotating.getClass().getSimpleName()
              + ": ");
          System.out.println("running="
              + cr.isRunning()
              + ", stopped="
              + cr.isStopped()
              + ", will stop="
              + cr.willStop());
          break;
        case PAUSED:
          System.out.print(imageAnimationRotating.getClass().getSimpleName()
              + ": ");
          System.out.println("paused="
              + cr.isPaused());
          break;
        case RESET:
          System.out.print(imageAnimationRotating.getClass().getSimpleName()
              + ": ");
          System.out.println("will reset="
              + cr.willReset());
          break;
        case WAIT:
          break;
        default:
          System.out.print(imageAnimationRotating.getClass().getSimpleName()
              + ": ");
          System.out.println("unknown");
          break;
      }
    } else if (cr == lImageAnimationLabelRotating.getImageAnimation()) {
      switch (e.getStateType()) {
        case RUNNING:
          System.out.print(lImageAnimationLabelRotating.getClass().getSimpleName()
              + ": ");
          System.out.println("running="
              + cr.isRunning()
              + ", stopped="
              + cr.isStopped()
              + ", will stop="
              + cr.willStop());
          break;
        case PAUSED:
          System.out.print(lImageAnimationLabelRotating.getClass().getSimpleName()
              + ": ");
          System.out.println("paused="
              + cr.isPaused());
          break;
        case RESET:
          System.out.print(lImageAnimationLabelRotating.getClass().getSimpleName()
              + ": ");
          System.out.println("will reset="
              + cr.willReset());
          break;
        case WAIT:
          break;
        default:
          System.out.print(lImageAnimationLabelRotating.getClass().getSimpleName()
              + ": ");
          System.out.println("unknown");
          break;
      }
    } else if (cr == imageAnimationSwapping) {
      switch (e.getStateType()) {
        case RUNNING:
          System.out.print(imageAnimationSwapping.getClass().getSimpleName()
              + ": ");
          System.out.println("running="
              + cr.isRunning()
              + ", stopped="
              + cr.isStopped()
              + ", will stop="
              + cr.willStop());
          break;
        case PAUSED:
          System.out.print(imageAnimationSwapping.getClass().getSimpleName()
              + ": ");
          System.out.println("paused="
              + cr.isPaused());
          break;
        case RESET:
          System.out.print(imageAnimationSwapping.getClass().getSimpleName()
              + ": ");
          System.out.println("will reset="
              + cr.willReset());
          break;
        case WAIT:
          break;
        default:
          System.out.print(imageAnimationSwapping.getClass().getSimpleName()
              + ": ");
          System.out.println("unknown");
          break;
      }
    }

  }


  @Override
  public void actionPerformed(ActionEvent e) {

    if (e.getSource() == bPauseRun) {
      if (bPauseRun.getText() == "Pause") {
        imageAnimationFading.pause(true);
      } else if (bPauseRun.getText() == "Run") {
        imageAnimationFading.pause(false);
      }
    } else if (e.getSource() == bStop) {
      imageAnimationFading.stop();
    } else if (e.getSource() == bReset) {
      imageAnimationFading.reset();
    }

  }



}
