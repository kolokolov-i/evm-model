package superbro.evm.core.device;

import superbro.evm.core.Device;
import superbro.evm.core.Machine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Display extends Device {

    public Display(Machine m) {
        super(m);
        autostart = true;
        palette = new Color[16];
        palette[0] = new Color(0x000000);
        palette[1] = new Color(0x0000AA);
        palette[2] = new Color(0x00AA00);
        palette[3] = new Color(0x00AAAA);
        palette[4] = new Color(0xAA0000);
        palette[5] = new Color(0xAA00AA);
        palette[6] = new Color(0xAA5500);
        palette[7] = new Color(0xAAAAAA);
        palette[8] = new Color(0x555555);
        palette[9] = new Color(0x5555FF);
        palette[10] = new Color(0x55FF55);
        palette[11] = new Color(0x55FFFF);
        palette[12] = new Color(0xFF5555);
        palette[13] = new Color(0xFF55FF);
        palette[14] = new Color(0xFFFF55);
        palette[15] = new Color(0xFFFFFF);
    }

    public static class Builder extends DeviceBuilder {
        @Override
        public Device create(Machine m) {
            return new Display(m);
        }
    }

    @Override
    public void launch() {
        JFrame frame = new JFrame("Display");
        frame.setResizable(false);
        frame.setBounds(100, 100, 650, 500);
        paintPanel = new STD_DrawPanel(this);
        frame.add(paintPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        repainter = new Thread(() -> {
            boolean flag = true;
            while (flag) {
                paintPanel.repaint();
                try {
                    Thread.sleep(50);
                } catch (
                        InterruptedException e) {
                    flag = false;
                }
            }
        });
        repainter.setDaemon(true);
        repainter.start();
    }

    @Override
    public JPanel getOptionsPane() {
        return null;
    }

    @Override
    public String getName() {
        return "Display";
    }

    @Override
    protected void handle(DeviceCall call) {
        switch (call.p0) {
            case (byte) 0x00:
                paintPanel.addSeg(coordX, coordY, call.p1, call.p2, call.p3 > 15 ? 0 : call.p3);
                coordX += call.p1;
                coordY += call.p2;
                break;
            case (byte) 0x01:
                coordX = call.p1;
                coordY = call.p2;
                break;
            case (byte) 0xFF:
                paintPanel.clearSeg();
                break;
            default:

        }
    }

    Thread repainter;
    STD_DrawPanel paintPanel;
    private boolean gridOn = true;
    private boolean showAxis = false;
    private int spaceW = 640;
    private int spaceH = 640;
    private int coordX, coordY;
    private Color gridColor = new Color(200, 200, 200);
    private Color palette[];

    private static class STD_DrawPanel extends JPanel
            implements MouseMotionListener, MouseListener, MouseWheelListener {
        private final Display holder;
        private float dxView, dyView;
        private int lastMx, lastMy, meshSize, iMeshSize, segCount, xh2, yh2;
        private final int meshScale[] = {3, 5, 10, 20, 30, 40, 50};
        private boolean dragView;
        private int segX[], segY[], segLx[], segLy[], segC[];
        private final int SEG_LIMIT = 10000;
        private BasicStroke stroke, axisStroke;

        public STD_DrawPanel(Display pHolder) {
            holder = pHolder;
            this.addMouseMotionListener(this);
            this.addMouseListener(this);
            this.addMouseWheelListener(this);
            setBackground(Color.WHITE);
            iMeshSize = 4;
            meshSize = meshScale[iMeshSize];
            xh2 = holder.spaceW / 2;
            yh2 = holder.spaceH / 2;
            stroke = new BasicStroke(3);
            axisStroke = new BasicStroke(3);

            segX = new int[SEG_LIMIT];
            segY = new int[SEG_LIMIT];
            segLx = new int[SEG_LIMIT];
            segLy = new int[SEG_LIMIT];
            segC = new int[SEG_LIMIT];
            segCount = 0;
            dxView = holder.spaceW / 2.f - this.getWidth()/(meshSize*2f);
            dyView = holder.spaceH / 2.f - this.getHeight()/(meshSize*2f);
        }

//        void relocate(){
//            dxView = holder.spaceW / 2.f - this.getWidth()/(meshSize*2f);
//            dyView = holder.spaceH / 2.f - this.getHeight()/(meshSize*2f);
//        }

        public void addSeg(int px, int py, int plx, int ply, int pc) {
            segX[segCount] = px;
            segY[segCount] = py;
            segLx[segCount] = plx;
            segLy[segCount] = ply;
            segC[segCount] = pc;
            segCount++;
        }

        public void clearSeg() {
            segCount = 0;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //dxView = 5;
            //dyView = 5;
            if (holder.gridOn) {
                int i, wn, hn, pw, ph;
                pw = this.getWidth();
                ph = this.getHeight();
                wn = pw / meshSize + 1;
                hn = ph / meshSize + 1;
                int dx = (int) dxView;
                dx = dx < 0 ? 0 : dx;
                int dy = (int) dyView;
                dy = dy < 0 ? 0 : dy;
                wn = (wn + dx) > holder.spaceW ? holder.spaceW - dx : wn + 2;
                hn = (hn + dy) > holder.spaceH ? holder.spaceH - dy : hn + 2;
                g.setColor(holder.gridColor);
                int x, y, mx, my, rx, ry;
                mx = (int) (-meshSize * (dxView - dx));
                my = (int) (-meshSize * (dyView - dy));
                rx = mx + (wn - 1) * meshSize;
                ry = my + (hn - 1) * meshSize;
                x = mx;
                y = my;
                for (i = 0; i < wn; i++) {
                    g.drawLine(x, my, x, ry);
                    x += meshSize;
                }
                for (i = 0; i < hn; i++) {
                    g.drawLine(mx, y, rx, y);
                    y += meshSize;
                }
            }
            if (holder.showAxis) {
                int x, y;
                int meshCountInFrameX, meshCountInFrameY, panelW, panelH;
                panelW = this.getWidth();
                panelH = this.getHeight();
                meshCountInFrameX = panelW / meshSize + 1;
                meshCountInFrameY = panelH / meshSize + 1;
                int meshOffsetX = (int) dxView;
                meshOffsetX = meshOffsetX < 0 ? 0 : meshOffsetX;
                int meshOffsetY = (int) dyView;
                meshOffsetY = meshOffsetY < 0 ? 0 : meshOffsetY;
                meshCountInFrameX = (meshCountInFrameX + meshOffsetX) > holder.spaceW ?
                        holder.spaceW - meshOffsetX : meshCountInFrameX + 2;
                meshCountInFrameY = (meshCountInFrameY + meshOffsetY) > holder.spaceH ?
                        holder.spaceH - meshOffsetY : meshCountInFrameY + 2;
                int frameXstart, frameYstart, frameXend, frameYend;
                frameXstart = (int) (-meshSize * (dxView - meshOffsetX));
                frameYstart = (int) (-meshSize * (dyView - meshOffsetY));
                frameXend = frameXstart + (meshCountInFrameX - 1) * meshSize;
                frameYend = frameYstart + (meshCountInFrameY - 1) * meshSize;

                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(axisStroke);
                g2.setColor(holder.gridColor);
                y = (int) ((yh2 - dyView) * meshSize);
                g2.drawLine(frameXstart, y, frameXend, y);
                x = (int) ((xh2 - dxView) * meshSize);
                g2.drawLine(x, frameYstart, x, frameYend);
            }
            // picture
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(stroke);
            for (int i = 0; i < segCount; i++) {
                int tx1, ty1, tx2, ty2;
                g2.setColor(holder.palette[segC[i]]);
                tx1 = (int)((segX[i]+xh2-dxView)*meshSize);
                tx2 = (int)((segX[i]+segLx[i]+xh2-dxView)*meshSize);
                ty1 = (int)((-segY[i]+yh2-dyView)*meshSize);
                ty2 = (int)((-segY[i]-segLy[i]+yh2-dyView)*meshSize);
                g2.drawLine(tx1, ty1, tx2, ty2);
            }
        }


        @Override
        public void mouseDragged(MouseEvent me) {
            if (dragView) {
                int cMx = me.getX();
                int cMy = me.getY();
                float dx = (cMx - lastMx) / (float) meshSize;
                float dy = (cMy - lastMy) / (float) meshSize;
                lastMx = cMx;
                lastMy = cMy;
                dxView -= dx;
                dyView -= dy;
            }
        }

        public void mouseMoved(MouseEvent me) {
        }

        public void mouseClicked(MouseEvent me) {
        }

        @Override
        public void mousePressed(MouseEvent me) {
            if (me.getButton() == MouseEvent.BUTTON2) {
                dragView = true;
                lastMx = me.getX();
                lastMy = me.getY();
            }
        }

        @Override
        public void mouseReleased(MouseEvent me) {
            if (me.getButton() == MouseEvent.BUTTON2) {
                dragView = false;
            }
        }

        public void mouseEntered(MouseEvent me) {
        }

        public void mouseExited(MouseEvent me) {
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent mwe) {
            int sc1 = meshSize;
            iMeshSize += mwe.getWheelRotation();
            if (iMeshSize < 0) {
                iMeshSize = 0;
            }
            if (iMeshSize > 6) {
                iMeshSize = 6;
            }
            meshSize = meshScale[iMeshSize];
            int sc2 = meshSize;
            float ddx = (this.getWidth() / (float) (2 * sc1)) - (this.getWidth() / (float) (2 * sc2));
            float ddy = (this.getHeight() / (float) (2 * sc1)) - (this.getHeight() / (float) (2 * sc2));
            dxView += ddx;
            dyView += ddy;
        }
    }

}
