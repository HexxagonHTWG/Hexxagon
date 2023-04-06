package di

import lib.controllerBaseImpl.Controller
import lib.{ControllerInterface, Player}

class FlexibleCoreModule(rows: Int, cols: Int):
  given ControllerInterface[Player] = Controller(
    using FlexibleProviderModule(rows, cols).given_FieldInterface_Player
  )(
    using FlexiblePersistenceModule(rows, cols).given_FileIOInterface
  )

object CoreModule:
  given ControllerInterface[Player] = Controller(
    using ProviderModule.given_FieldInterface_Player
  )(
    using PersistenceModule.given_FileIOInterface
  )

