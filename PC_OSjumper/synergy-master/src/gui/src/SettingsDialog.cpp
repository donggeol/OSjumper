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

#include "SettingsDialog.h"
#include "SynergyLocale.h"
#include "QSynergyApplication.h"
#include "QUtility.h"
#include "AppConfig.h"

#include <QtCore>
#include <QtGui>
#include <QMessageBox>
#include <QFileDialog>

SettingsDialog::SettingsDialog(QWidget* parent, AppConfig& config) :
	QDialog(parent, Qt::WindowTitleHint | Qt::WindowSystemMenuHint),
	Ui::SettingsDialogBase(),
	m_AppConfig(config)
{
	setupUi(this);

	m_Locale.fillLanguageComboBox(m_pComboLanguage);

	m_pLineEditScreenName->setText(appConfig().screenName());
	m_pSpinBoxPort->setValue(appConfig().port());
	setIndexFromItemData(m_pComboLanguage, appConfig().language());
}

void SettingsDialog::accept()
{
	appConfig().setScreenName(m_pLineEditScreenName->text());
	appConfig().setPort(m_pSpinBoxPort->value());
	appConfig().setLanguage(m_pComboLanguage->itemData(m_pComboLanguage->currentIndex()).toString());
	appConfig().saveSettings();
	QDialog::accept();
}

void SettingsDialog::reject()
{
	QSynergyApplication::getInstance()->switchTranslator(appConfig().language());
	QDialog::reject();
}

void SettingsDialog::changeEvent(QEvent* event)
{
	if (event != 0)
	{
		switch (event->type())
		{
		case QEvent::LanguageChange:
			{

				m_pComboLanguage->blockSignals(true);
				retranslateUi(this);
				m_pComboLanguage->blockSignals(false);

				break;
			}

		default:
			QDialog::changeEvent(event);
		}
	}
}


void SettingsDialog::on_m_pComboLanguage_currentIndexChanged(int index)
{
	QString ietfCode = m_pComboLanguage->itemData(index).toString();
	QSynergyApplication::getInstance()->switchTranslator(ietfCode);
}
