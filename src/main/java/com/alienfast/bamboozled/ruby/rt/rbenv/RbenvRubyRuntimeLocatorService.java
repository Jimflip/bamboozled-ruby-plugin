package com.alienfast.bamboozled.ruby.rt.rbenv;

import java.io.File;

import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alienfast.bamboozled.ruby.rt.RubyLocator;
import com.alienfast.bamboozled.ruby.rt.RubyRuntimeLocatorService;
import com.alienfast.bamboozled.ruby.util.FileSystemHelper;

/**
 *
 */
public class RbenvRubyRuntimeLocatorService implements RubyRuntimeLocatorService {

    private static final Logger log = LoggerFactory.getLogger( RbenvRubyRuntimeLocatorService.class );

    private static final String RBENV_DEFAULT_PATH = ".rbenv";

    public static final String MANAGER_LABEL = "rbenv";

    private final FileSystemHelper fileSystemHelper;

    public RbenvRubyRuntimeLocatorService() {

        this( new FileSystemHelper() );
    }

    public RbenvRubyRuntimeLocatorService(FileSystemHelper fileSystemHelper) {

        this.fileSystemHelper = fileSystemHelper;
    }

    @Override
    public RubyLocator getRubyLocator() {

        // No RVM on windows at the moment.
        if ( SystemUtils.IS_OS_WINDOWS ) {
            log.warn( "Windows isn't support for RVM installations" );
            return null;
        }

        final String userRbenvInstallPath = this.fileSystemHelper.getUserHome() + File.separator + RBENV_DEFAULT_PATH;

        log.info( "Searching for rbenv installation in users home directory located at - {}", userRbenvInstallPath );

        // Is rbenv is installed in the users home directory
        if ( this.fileSystemHelper.pathExists( userRbenvInstallPath ) ) {
            return checkRbenvInstallation( userRbenvInstallPath );
        }

        // none found
        return null;
    }

    private RubyLocator checkRbenvInstallation( String userRbenvInstallPath ) {

        // check whether the install directory
        final String rubiesPath = RbenvUtils.buildRbenvRubiesPath( userRbenvInstallPath );

        this.fileSystemHelper.assertPathExists( rubiesPath, "Rbenv Installation missing rubies directory" );

        // return the rbenv ruby locator
        return new RbenvRubyLocator( this.fileSystemHelper, userRbenvInstallPath );
    }

    @Override
    public boolean isInstalled() {

        return getRubyLocator() != null;
    }

    @Override
    public String getRuntimeManagerName() {

        return MANAGER_LABEL;
    }
}
