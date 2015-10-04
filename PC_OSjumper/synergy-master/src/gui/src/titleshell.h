#ifndef TITLEHELL_H
#define TITLEHELL_H

#include <QWidget>
#include <QtGui>


class TitleShell : public QWidget
{
public:
    TitleShell(QWidget* rhs) ;

protected:
    void mousePressEvent(QMouseEvent *me);
    void mouseMoveEvent(QMouseEvent *me);
    void mouseReleaseEvent(QMouseEvent *);

private:
    QToolButton *minimize;
    QToolButton *maximize;
    QToolButton *close;
    QPixmap restorePix, maxPix;
    bool maxNormal;
    QPoint startPos;
    QPoint clickPos;
    bool      m_titleBarPress;

};

#endif // TITLEHELL_H
