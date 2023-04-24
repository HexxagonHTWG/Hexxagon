package di

import lib.defaultImpl.Controller
import lib.{ControllerInterface, CoreRestClient, Player}

class FlexibleCoreModule(rows: Int, cols: Int):
  given ControllerInterface[Player] = Controller(
    using FlexibleProviderModule(rows, cols).given_FieldInterface_Player
  )(
    using PersistenceModule.given_FileIOInterface_Player
  )

object CoreModule:
  given ControllerInterface[Player] = Controller(
    using ProviderModule.given_FieldInterface_Player
  )(
    using PersistenceModule.given_FileIOInterface_Player
  )

object CoreServerModule:
  given ControllerInterface[Player] = Controller(
    using ProviderModule.given_FieldInterface_Player
  )(
    using PersistenceRestModule.given_FileIOInterface_Player
  )

object CoreRestModule:
  given ControllerInterface[Player] = CoreRestClient()
