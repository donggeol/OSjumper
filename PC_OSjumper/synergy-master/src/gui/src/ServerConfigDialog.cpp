/*
 * synergy -- mouse and keyboard sharing utility
 * Copyright (C) 2012 Bolton Software Ltd.
 * Copyright (C) 2008 Volker Lanz (vl@fidra.de)
 * 
 * This package is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * found in the file COPYING that should have accompanied this file.
 * 
 * This package is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

#include "ServerConfigDialog.h"
#include "ServerConfig.h"
#include "ActionDialog.h"

#include <QtCore>
#include <QtGui>
#include<QMessageBox>

ServerConfigDialog::ServerConfigDialog(QWidget* parent, ServerConfig& config, const QString& defaultScreenName) :
	QDialog(parent, Qt::WindowTitleHint | Qt::WindowSystemMenuHint),
	Ui::ServerConfigDialogBase(),
	m_OrigServerConfig(config),
	m_ServerConfig(config),
	m_ScreenSetupModel(serverConfig().screens(), serverConfig().numColumns(), serverConfig().numRows())
{
	setupUi(this);


	m_pScreenSetupView->setModel(&m_ScreenSetupModel);

	if (serverConfig().numScreens() == 0)
		model().screen(serverConfig().numColumns() / 2, serverConfig().numRows() / 2) = Screen(defaultScreenName);
}

void ServerConfigDialog::accept()
{
	// now that the dialog has been accepted, copy the new server config to the original one,
	// which is a reference to the one in MainWindow.
	setOrigServerConfig(serverConfig());

	QDialog::accept();
}
