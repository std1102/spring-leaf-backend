package com.springleaf;

import com.springleaf.common.Config;
import com.springleaf.database.DataSourceHandle;
import com.springleaf.rest.Router;

/**
 * Spring leaf
 *
 */
public class App 
{
    public static void main( String[] args ) throws InstantiationException, IllegalAccessException {
        System.out.println( "\n" +
                "░██████╗██████╗░██████╗░██╗███╗░░██╗░██████╗░  ██╗░░░░░███████╗░█████╗░███████╗\n" +
                "██╔════╝██╔══██╗██╔══██╗██║████╗░██║██╔════╝░  ██║░░░░░██╔════╝██╔══██╗██╔════╝\n" +
                "╚█████╗░██████╔╝██████╔╝██║██╔██╗██║██║░░██╗░  ██║░░░░░█████╗░░███████║█████╗░░\n" +
                "░╚═══██╗██╔═══╝░██╔══██╗██║██║╚████║██║░░╚██╗  ██║░░░░░██╔══╝░░██╔══██║██╔══╝░░\n" +
                "██████╔╝██║░░░░░██║░░██║██║██║░╚███║╚██████╔╝  ███████╗███████╗██║░░██║██║░░░░░\n" +
                "╚═════╝░╚═╝░░░░░╚═╝░░╚═╝╚═╝╚═╝░░╚══╝░╚═════╝░  ╚══════╝╚══════╝╚═╝░░╚═╝╚═╝░░░░░" );
        Config.init();
        DataSourceHandle.init();
        Router.init();
    }
}
