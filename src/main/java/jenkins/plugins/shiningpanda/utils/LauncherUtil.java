/*
 * ShiningPanda plug-in for Jenkins
 * Copyright (C) 2011-2012 ShiningPanda S.A.S.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jenkins.plugins.shiningpanda.utils;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.TaskListener;
import hudson.tasks.Messages;
import hudson.util.ArgumentListBuilder;

import java.io.IOException;

import jenkins.plugins.shiningpanda.workspace.Workspace;

public class LauncherUtil
{

    /**
     * Launch a process.
     * 
     * @param launcher
     *            The launcher
     * @param listener
     *            The build listener
     * @param workspace
     *            The workspace
     * @param environment
     *            The environment
     * @param args
     *            The arguments
     * @return true if was successful, else false
     * @throws InterruptedException
     */
    public static boolean launch(Launcher launcher, TaskListener listener, Workspace workspace, EnvVars environment,
            ArgumentListBuilder args) throws InterruptedException
    {
        // Be able to display error
        try
        {
            // Launch the process
            return 0 == launcher.launch().cmds(workspace.isUnix() ? args : args.toWindowsCommand()).envs(environment)
                    .stdout(listener).pwd(workspace.getHome()).join();
        }
        catch (IOException e)
        {
            // Something went wrong, display error
            Util.displayIOException(e, listener);
            // Log error message
            e.printStackTrace(listener.fatalError(Messages.CommandInterpreter_CommandFailed()));
            // Return an error
            return false;
        }
    }

    /**
     * Create a symbolic link.
     * 
     * @param launcher
     *            The launcher
     * @param listener
     *            The listener
     * @param target
     *            The target file
     * @param link
     *            The link file
     * @return true if successful, else false
     * @throws InterruptedException
     * @throws IOException
     */
    public static boolean createSymlink(Launcher launcher, TaskListener listener, FilePath target, FilePath link)
            throws InterruptedException, IOException
    {
        // Get the arguments
        ArgumentListBuilder args = new ArgumentListBuilder("ln", "-s", target.getRemote(), link.getRemote());
        // Be able to display error
        try
        {
            // Launch the process
            return 0 == launcher.launch().cmds(args).stdout(listener).join();
        }
        catch (IOException e)
        {
            // Something went wrong, display error
            Util.displayIOException(e, listener);
            // Log error message
            e.printStackTrace(listener.fatalError(Messages.CommandInterpreter_CommandFailed()));
            // Return an error
            return false;
        }
    }
}