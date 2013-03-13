/*
 * This file is part of the Adblock Plus,
 * Copyright (C) 2006-2012 Eyeo GmbH
 *
 * Adblock Plus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as
 * published by the Free Software Foundation.
 *
 * Adblock Plus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Adblock Plus.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.adblockplus.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Starter extends BroadcastReceiver
{

  @Override
  public void onReceive(Context context, Intent intent)
  {
    String action = intent.getAction();
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    boolean enabled = prefs.getBoolean(context.getString(R.string.pref_enabled), false);
    boolean proxyenabled = prefs.getBoolean(context.getString(R.string.pref_proxyenabled), false);
    if (Intent.ACTION_PACKAGE_REPLACED.equals(action))
    {
      String pkg = context.getApplicationInfo().packageName;
      boolean us = pkg.equals(intent.getData().getSchemeSpecificPart());
      enabled &= us;
      proxyenabled &= us;
    }
    if (Intent.ACTION_BOOT_COMPLETED.equals(action))
    {
      boolean startAtBoot = prefs.getBoolean(context.getString(R.string.pref_startatboot), context.getResources().getBoolean(R.bool.def_startatboot));
      enabled &= startAtBoot;
      proxyenabled &= startAtBoot;
    }
    if (enabled)
    {
      AdblockPlus application = AdblockPlus.getApplication();
      application.setFilteringEnabled(true);
      application.startEngine();
    }
    if (proxyenabled)
      context.startService(new Intent(context, ProxyService.class));
  }

}
