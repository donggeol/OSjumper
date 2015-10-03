#include "titlebar.h"

#include <QtGui>

    TitleBar::TitleBar(QWidget *parent)
    {
        // Don't let this widget inherit the parent's backround color
        setAutoFillBackground(true);
        // Use a brush with a Highlight color role to render the background
        setBackgroundRole(QPalette::Highlight);

        minimize = new QToolButton(this);
        maximize = new QToolButton(this);
        close= new QToolButton(this);

        // Use the style to set the button pixmaps
        QPixmap pix = style()->standardPixmap(QStyle::SP_TitleBarCloseButton);
        close->setIcon(pix);

        maxPix = style()->standardPixmap(QStyle::SP_TitleBarMaxButton);
        maximize->setIcon(maxPix);

        pix = style()->standardPixmap(QStyle::SP_TitleBarMinButton);
        minimize->setIcon(pix);

        restorePix = style()->standardPixmap(QStyle::SP_TitleBarNormalButton);

        minimize->setMinimumHeight(20);
        close->setMinimumHeight(20);
        maximize->setMinimumHeight(20);


        QLabel *label = new QLabel(this);
        label->setText("OS Jumper");
        parent->setWindowTitle("OS Jumper");

        QHBoxLayout *hbox = new QHBoxLayout(this);

        hbox->addWidget(label);
        hbox->addWidget(minimize);
        hbox->addWidget(maximize);
        hbox->addWidget(close);

        hbox->insertStretch(1, 500);
        hbox->setSpacing(0);
        setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);

        maxNormal = false;

        connect(close, SIGNAL( clicked() ), parent, SLOT(close() ) );
        connect(minimize, SIGNAL( clicked() ), this, SLOT(showSmall() ) );
        connect(maximize, SIGNAL( clicked() ), this, SLOT(showMaxRestore() ) );


        m_titleBarPress = false;
    }

    void TitleBar::showSmall()
    {
        parentWidget()->parentWidget()->showMinimized();
    }

    void TitleBar::showMaxRestore()
    {
        if (maxNormal) {
            parentWidget()->parentWidget()->showNormal();
            maxNormal = !maxNormal;
            maximize->setIcon(maxPix);
        } else {
            parentWidget()->parentWidget()->showMaximized();
            maxNormal = !maxNormal;
            maximize->setIcon(restorePix);
        }
    }
