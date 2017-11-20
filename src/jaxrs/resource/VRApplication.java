package jaxrs.resource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/*
 * JAX-RSの全体設定として、リクエスト時のアプリケーションパスを"rest"に設定する
 */
@ApplicationPath("rest")
public class VRApplication extends Application {
}
