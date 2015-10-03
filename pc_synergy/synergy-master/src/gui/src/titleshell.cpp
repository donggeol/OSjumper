#include "titleshell.h"

TitleShell::TitleShell(QWidget* rhs) : QWidget(rhs)
{


}

void TitleShell::mousePressEvent(QMouseEvent *me)
{
    startPos = me->globalPos();
    clickPos = mapToParent(me->pos());
    m_titleBarPress = true;

}
void TitleShell::mouseMoveEvent(QMouseEvent *me)
{
   if (maxNormal || !m_titleBarPress)    return;

   parentWidget()->move(me->globalPos() - clickPos);
}

void TitleShell::mouseReleaseEvent(QMouseEvent *)
{
       m_titleBarPress = false;
}

