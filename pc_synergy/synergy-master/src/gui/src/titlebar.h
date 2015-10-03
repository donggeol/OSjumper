#ifndef TITLEBAR_H
#define TITLEBAR_H

#include <QWidget>

#include <QtGui>

class TitleBar : public QWidget
{
    Q_OBJECT
public:
    TitleBar(QWidget *parent);

public slots:
    void showSmall();

    void showMaxRestore();
protected:

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


#endif // TITLEBAR_H
